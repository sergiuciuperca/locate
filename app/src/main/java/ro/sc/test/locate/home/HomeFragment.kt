package ro.sc.test.locate.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ro.sc.test.locate.R
import ro.sc.test.locate.databinding.FragmentHomeBinding


@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    private val homeAdapter: HomeAdapter by lazy {
        val cornerSize = resources.getDimensionPixelSize(R.dimen.item_home_corner)
        return@lazy HomeAdapter(cornerSize)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().window.statusBarColor = ResourcesCompat.getColor(resources, R.color.home_background, null)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerJobs.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            itemAnimator = DefaultItemAnimator()
            adapter = homeAdapter
        }

        viewModel.stateFlow.onEach {
            handle(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }


    private fun handle(state: HomeViewState) {
        homeAdapter.updateData(state.items)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}