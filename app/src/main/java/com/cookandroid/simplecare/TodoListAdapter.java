package com.cookandroid.simplecare;
import android.content.Context;
import android.widget.ArrayAdapter;
import java.util.List;

public class TodoListAdapter extends ArrayAdapter<String> {
    public TodoListAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
    }

    // 추가적인 커스터마이징이 필요하면 여기에 작성할 수 있습니다.
}
