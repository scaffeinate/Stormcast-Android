package stormcast.app.phoenix.io.stormcast.activities;

import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import stormcast.app.phoenix.io.stormcast.R;
import stormcast.app.phoenix.io.stormcast.databinding.ActivityMainBinding;
import stormcast.app.phoenix.io.stormcast.fragments.EditLocationsFragment;
import stormcast.app.phoenix.io.stormcast.fragments.ForecastFragment;
import stormcast.app.phoenix.io.stormcast.views.toolbar.AnimatedActionBarDrawerToggle;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ToolbarCallbacks, FragmentManager.OnBackStackChangedListener {

    private static final String NAV_ITEM_TITLE = "navItemTitle";
    private static final String NAV_ITEM_INDEX = "navItemIndex";

    private ActivityMainBinding mBinding;
    private FragmentManager mFragmentManager;

    private ActionBar mActionBar;
    private AnimatedActionBarDrawerToggle mDrawerToggle;
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

        mFragmentManager.addOnBackStackChangedListener(this);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
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
                case R.id.action_edit_locations:
                    fragment = EditLocationsFragment.newInstance();
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
        mActionBar = getSupportActionBar();
        mDrawerToggle = new AnimatedActionBarDrawerToggle(this, mBinding.drawerLayout,
                mBinding.toolbar.toolbar, R.string.open, R.string.close);
        mDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mBinding.drawerLayout.addDrawerListener(mDrawerToggle);
    }

    @Override
    public void setToolbarTitle(String title) {
        mBinding.toolbar.textViewToolbarTitle.setText(title);
    }

    @Override
    public void setToolbarTextColor(int color) {
        mBinding.toolbar.textViewToolbarTitle.setTextColor(color);
    }

    @Override
    public void setToolbarBackgroundColor(int color) {
        mBinding.toolbar.toolbar.setBackgroundColor(color);
    }

    private void closeDrawer() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                mBinding.drawerLayout.closeDrawer(GravityCompat.START);
            }
        });
    }

    @Override
    public void onBackStackChanged() {
        boolean showBackButton  = (mFragmentManager.getBackStackEntryCount() > 0);
        if(showBackButton) {
            mDrawerToggle.setDrawerIndicatorEnabled(false);
            mActionBar.setDisplayHomeAsUpEnabled(true);
            mDrawerToggle.animateToBackArrow();
        } else {
            mActionBar.setDisplayHomeAsUpEnabled(false);
            mDrawerToggle.animateToMenu();
            mDrawerToggle.setDrawerIndicatorEnabled(true);
        }
    }
}
