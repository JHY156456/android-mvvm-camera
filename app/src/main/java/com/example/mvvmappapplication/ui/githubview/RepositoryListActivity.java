package com.example.mvvmappapplication.ui.githubview;

import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mvvmappapplication.R;
import com.example.mvvmappapplication.data.githubcontract.RepositoryListViewContract;
import com.example.mvvmappapplication.data.githubmodel.GitHubService;
import com.example.mvvmappapplication.databinding.ActivityRepositoryListBinding;
import com.example.mvvmappapplication.ui.githubviewmodel.RepositoryListViewModel;
import com.google.android.material.snackbar.Snackbar;

/**
 * 리포지토리 목록을 표시하는 액티비티
 * MVVM의 뷰 역할을 한다
 */
public class RepositoryListActivity extends AppCompatActivity implements RepositoryListViewContract {

  private Spinner languageSpinner;
  private CoordinatorLayout coordinatorLayout;

  private RepositoryAdapter repositoryAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ActivityRepositoryListBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_repository_list);
    final GitHubService gitHubService = ((NewGitHubReposApplication) getApplication()).getGitHubService();
    binding.setViewModel(new RepositoryListViewModel((RepositoryListViewContract) this, gitHubService));

    // 뷰를 셋업
    setupViews();
  }

  /**
   * 목록 등 화면 요소를 만든다
   */
  private void setupViews() {
    // 툴바 설정
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

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
