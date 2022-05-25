package thoreros.lamp.controller.Fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import thoreros.lamp.controller.R;

public class FireConfigFragment extends ModeConfigFragment {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.fire_config, rootKey);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //this.getListView().setOverScrollMode(ListView.OVER_SCROLL_NEVER);
    }
}
