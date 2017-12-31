package stormcast.app.phoenix.io.stormcast.activities;

import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import stormcast.app.phoenix.io.stormcast.R;
import stormcast.app.phoenix.io.stormcast.databinding.ActivityMainBinding;
import stormcast.app.phoenix.io.stormcast.fragments.ForecastFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String NAV_ITEM_TITLE = "navItemTitle";
    private static final String NAV_ITEM_INDEX = "navItemIndex";

    private ActivityMainBinding mBinding;
    private FragmentManager mFragmentManager;

    private ActionBarDrawerToggle mDrawerToggle;
    private int selectedNavItem = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        mFragmentManager = getSupportFragmentManager();
        setSupportActionBar(mBinding.toolbar.toolbar);
        mBinding.navigationView.setNavigationItemSelectedListener(this);

        setupToolbar();

        if (savedInstanceState == null) {
            Menu menu = mBinding.navigationView.getMenu();
            onNavigationItemSelected(menu.findItem(R.id.action_forecast));
        } else {
            setToolbarTitle(savedInstanceState.getString(NAV_ITEM_TITLE));
            selectedNavItem = savedInstanceState.getInt(NAV_ITEM_INDEX);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        selectDrawerItem(item);
        return true;
    }

    private void selectDrawerItem(MenuItem item) {
        if (item.getItemId() != selectedNavItem) {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.action_forecast:
                    fragment = ForecastFragment.newInstance();
                    break;
            }

            if (fragment != null) {
                mFragmentManager.beginTransaction()
                        .replace(R.id.layout_content, fragment)
                        .commit();
            }

            mBinding.navigationView.setCheckedItem(item.getItemId());
            selectedNavItem = item.getItemId();
            setToolbarTitle(item.getTitle().toString());
        }

        closeDrawer();
    }

    private void setupToolbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mDrawerToggle = new ActionBarDrawerToggle(this, mBinding.drawerLayout, mBinding.toolbar.toolbar, R.string.open, R.string.close);
        mBinding.drawerLayout.addDrawerListener(mDrawerToggle);
    }

    private void setToolbarTitle(String title) {
        mBinding.toolbar.textViewToolbarTitle.setText(title);
    }

    private void closeDrawer() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                mBinding.drawerLayout.closeDrawer(GravityCompat.START);
            }
        });
    }
}
