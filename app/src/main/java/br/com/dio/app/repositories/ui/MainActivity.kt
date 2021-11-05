package br.com.dio.app.repositories.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import br.com.dio.app.repositories.R
import br.com.dio.app.repositories.core.createDialog
import br.com.dio.app.repositories.core.createProgressDialog
import br.com.dio.app.repositories.core.hasInternet
import br.com.dio.app.repositories.core.hideSoftKeyboard
import br.com.dio.app.repositories.data.model.Owner
import br.com.dio.app.repositories.databinding.ActivityMainBinding
import br.com.dio.app.repositories.presentation.MainViewModel
import com.bumptech.glide.Glide
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    private val dialog by lazy { createProgressDialog() }
    private val viewModel by viewModel<MainViewModel>()
    private val adapter by lazy {
        RepoListAdapter { repo ->
            openLink(repo.htmlURL)
        }
    }

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        binding.rvRepos.adapter = adapter

        bindOwnerProfile()


        viewModel.repos.observe(this) {

            when (it) {
                MainViewModel.State.Loading -> dialog.show()
                is MainViewModel.State.Error -> {

                    if (!hasInternet()) {
                        hasInternet()
                        //Toast.makeText(this,R.string.has_internet,Toast.LENGTH_SHORT).show()
                    } else {
                        createDialog {
                            setMessage(it.error.message)
                            Log.e("TAG", "Erro UsuarioMainActivity")
                        }.show()
                    }
                    dialog.dismiss()
                }
                is MainViewModel.State.Success -> {
                    dialog.dismiss()
                    binding.includeEmpty.emptyState.visibility = if (it.list.isEmpty()) View.VISIBLE
                    else View.GONE
                    adapter.submitList(it.list)
                }

            }

        }
    }

    override fun onStart() {
        if (!hasInternet()) {
            Toast.makeText(this, R.string.has_internet, Toast.LENGTH_SHORT).show()
            Log.e("TAG", "Erro OnStart")
        }

        super.onStart()
    }

    private fun bindOwnerProfile() {
        viewModel.owner.observe(this) {
            when (it) {
                is MainViewModel.OwnerState.Success -> {
                    val owner = it.owner
                    setOwnerAvatar(owner.avatarURL)
                    binding.tvRepoName.text = owner.name
                    binding.tvRepo.text = getString(R.string.n_repositories, owner.publicRepos)
                    Log.e("TAG", "bindOwner")
                }

            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val searchView = menu.findItem(R.id.action_search).actionView as SearchView
        searchView.setOnQueryTextListener(this)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        query?.let { viewModel.getRepoList(it) }
        binding.root.hideSoftKeyboard()
        if (!hasInternet()) {
            Toast.makeText(this, R.string.has_internet, Toast.LENGTH_SHORT).show()
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        Log.e(TAG, "onQueryTextChange: $newText")

        return false
    }

    companion object {
        private const val TAG = "TAG"
    }

    private fun openLink(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    private fun setOwnerAvatar(avatarURL: String) {

        Glide.with(binding.root.context)
            .load(Uri.parse(avatarURL))
            .into(binding.homeUserAvatarIv)
    }

}