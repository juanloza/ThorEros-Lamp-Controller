package thoreros.libraries.preference.palette;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import thoreros.libraries.preference.R;

public class PaletteAdapter extends BaseAdapter {
    private final Context context;
    private final CharSequence[] names;
    private final int[] imageResIds;
    private final int selected;
    private final int layoutId;
    private final int labelId;
    private final int checkableId;

    public PaletteAdapter(Context context, CharSequence[] names, int[] imageResIds,
                                int selected, int layoutId, int labelId, int checkableId) {
        this.context = context;
        this.names = names;
        this.imageResIds = imageResIds;
        this.selected = selected;
        this.layoutId = layoutId;
        this.labelId = labelId;
        this.checkableId = checkableId;
    }

    @Override
    public int getCount() {
        return names.length;
    }

    @Override
    public Object getItem(int position) {
        return names[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(layoutId, parent, false);
            TextView label = view.findViewById(R.id.palette_name);
            RadioButton button = view.findViewById(checkableId);
            ImageView image = view.findViewById(R.id.palette_image);
            view.setTag(new Holder(label, button, image));
        }
        Holder holder = (Holder) view.getTag();
        holder.label.setText(names[position]);
        holder.checkable.setChecked(position == selected);
        if(imageResIds.length >= position) {
            holder.image.setImageResource(imageResIds[position]);
        }
        return view;
    }

    private static class Holder {

        public final TextView label;
        public final Checkable checkable;
        public final ImageView image;

        public Holder(TextView label, Checkable button, ImageView image) {
            this.label = label;
            this.checkable = button;
            this.image = image;
        }
    }
}
