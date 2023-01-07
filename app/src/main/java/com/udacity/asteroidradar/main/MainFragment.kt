package com.udacity.asteroidradar.main

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.adapters.AsteroidAdapter
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {
    private lateinit var adapter: AsteroidAdapter
    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        adapter = AsteroidAdapter(AsteroidAdapter.OnClickListener {
            viewModel.dispDetailsStart(it)
        })
        binding.asteroidRecycler.adapter = adapter

        viewModel.navtoDetails.observe(viewLifecycleOwner) {
            if (it != null) {
                findNavController().navigate(
                    MainFragmentDirections.actionShowDetail(it)
                )
                viewModel.dispDetailsEnd()
            }
        }
        viewModel.asteroidList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            Log.i("MainFragment", it.toString())
        }
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.week -> {
                viewModel.getWeekAsteroids().observe(viewLifecycleOwner) {
                    adapter.submitList(it)
                }
            }

            R.id.today -> {
                viewModel.getTodayAsteroids().observe(viewLifecycleOwner) {
                    adapter.submitList(it)
                }
            }

            R.id.saved -> {
                viewModel.asteroidList.observe(viewLifecycleOwner) {
                    adapter.submitList(it)
                }
            }
        }
        return true
    }

}