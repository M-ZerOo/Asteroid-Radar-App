package com.melfouly.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.melfouly.asteroidradar.R
import com.melfouly.asteroidradar.databinding.FragmentMainBinding
import com.melfouly.asteroidradar.model.Asteroid


class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }

    private lateinit var adapter: MainAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        setHasOptionsMenu(true)

        adapter = MainAdapter(MainAdapter.AsteroidListener { asteroid ->
            viewModel.onAsteroidClicked(asteroid)
        })

        binding.asteroidRecycler.adapter = adapter

        viewModel.allAsteroids.observe(viewLifecycleOwner) { asteroids ->
            asteroids?.let { adapter.submitList(asteroids) }
        }

        viewModel.detailedAsteroid.observe(viewLifecycleOwner) { asteroid ->
            asteroid?.let {
                this.findNavController().navigate(MainFragmentDirections.actionMainFragmentToDetailsFragment(it))
                viewModel.doneNavigating()
            }
        }
        return binding.root
    }

    // Navigate to details fragment and call doneNavigating to return values to null
    private fun navigateToDetailsFragment(asteroid: Asteroid) {
        findNavController().navigate(
            MainFragmentDirections.actionMainFragmentToDetailsFragment(
                asteroid
            )
        )
        viewModel.doneNavigating()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.show_week -> viewModel.getWeeklyAsteroids()
            R.id.show_today -> viewModel.getTodayAsteroids()
            R.id.show_saved -> viewModel.getSavedAsteroids()
        }
        return true
    }
}