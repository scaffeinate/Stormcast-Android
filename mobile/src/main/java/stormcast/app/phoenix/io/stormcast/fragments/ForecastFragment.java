package stormcast.app.phoenix.io.stormcast.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import stormcast.app.phoenix.io.stormcast.R;
import stormcast.app.phoenix.io.stormcast.databinding.FragmentHomeBinding;

/**
 * Created by sudharti on 12/31/17.
 */

public class ForecastFragment extends Fragment implements View.OnClickListener {

    private FragmentHomeBinding mBinding;
    private FragmentManager mFragmentManager;

    public static ForecastFragment newInstance() {
        return new ForecastFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);

        mFragmentManager = getActivity().getSupportFragmentManager();

        mBinding.btnAddLocation.setOnClickListener(this);

        return mBinding.getRoot();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add_location:
                mFragmentManager.beginTransaction()
                        .replace(R.id.layout_content, AddLocationFragment.newInstance(false))
                        .commit();
                break;
        }
    }
}
