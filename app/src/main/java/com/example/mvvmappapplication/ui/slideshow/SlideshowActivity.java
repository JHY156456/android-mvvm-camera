package com.example.mvvmappapplication.ui.slideshow;

import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mvvmappapplication.R;
import com.example.mvvmappapplication.data.githubcontract.RepositoryListViewContract;
import com.example.mvvmappapplication.data.githubmodel.GitHubService;
import com.example.mvvmappapplication.databinding.FragmentSlideshowBinding;
import com.example.mvvmappapplication.di.AppViewModelFactory;
import com.example.mvvmappapplication.ui.BaseActivity;
import com.example.mvvmappapplication.ui.githubview.DetailActivity;
import com.example.mvvmappapplication.ui.githubview.RepositoryAdapter;
import com.google.android.material.snackbar.Snackbar;

import javax.inject.Inject;

import dagger.Lazy;


public class SlideshowActivity extends BaseActivity implements RepositoryListViewContract {

    @Inject
    Lazy<FragmentSlideshowBinding> binding;
    SlideshowViewModel viewModel;
    @Inject
    AppViewModelFactory viewModelFactory;
    private Spinner languageSpinner;
    private CoordinatorLayout coordinatorLayout;

    private RepositoryAdapter repositoryAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding.get().setLifecycleOwner(this);
        viewModel = new ViewModelProvider(this, viewModelFactory).get(SlideshowViewModel.class);
        binding.get().setViewModel(viewModel);

        super.setDrawerLayoutAndToolbar();
        super.setToolbarTitleNoCollapsing("저장소");
        super.setAppBarConfigurationForLeftMenuIcon();
        super.setAppBarLayout(true);

        setupViews();
        initLiveData();
//        viewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                binding.get().textSlideshow.setText(s);
//            }
//        });
    }

    private void initLiveData(){
        viewModel.getRepositoriesMutableLiveData().observe(this,repositories -> {
            repositoryAdapter.setItemsAndRefresh(repositories.items);
        });
        viewModel.errorEvent.observe(this,throwable -> {
            Snackbar.make(coordinatorLayout, "읽을 수 없습니다", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        });
    }
    /**
     * 목록 등 화면 요소를 만든다
     */
    private void setupViews() {
        // Recycler View
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_repos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        repositoryAdapter = new RepositoryAdapter((Context) this, (RepositoryListViewContract) this);
        recyclerView.setAdapter(repositoryAdapter);

        // SnackBar 표시에서 이용한다
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);

        // Spinner
        languageSpinner = (Spinner) findViewById(R.id.language_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        adapter.addAll("java", "objective-c", "swift", "groovy", "python", "ruby", "c");
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSpinner.setAdapter(adapter);
    }

    // =====RepositoryListViewContract 구현=====
    // 여기서 Presenter로부터 지시를 받아 뷰의 변경 등을 한다

    @Override
    public void startDetailActivity(String full_name) {
        DetailActivity.start(this, full_name);
    }


    @Override
    public void showRepositories(GitHubService.Repositories repositories) {
        repositoryAdapter.setItemsAndRefresh(repositories.items);
    }

    @Override
    public void showError() {
        Snackbar.make(coordinatorLayout, "읽을 수 없습니다", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
}