package com.example.myapplication.adapters;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.example.myapplication.models.UserBoundary;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private final List<UserBoundary> userList;

    public UserAdapter(List<UserBoundary> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_bubble, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        UserBoundary user = userList.get(position);
        holder.userSystemId.setText("System ID: " + user.getUserId().getSystemID());
        holder.userEmail.setText("Email: " + user.getUserId().getEmail());
        holder.userRole.setText("Role: " + user.getRole().name());
        holder.userUsername.setText("Username: " + user.getUsername());
        holder.userAvatar.setText("Avatar: " + user.getAvatar());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView userSystemId, userEmail, userRole, userUsername, userAvatar;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            userSystemId = itemView.findViewById(R.id.userSystemId);
            userEmail = itemView.findViewById(R.id.userEmail);
            userRole = itemView.findViewById(R.id.userRole);
            userUsername = itemView.findViewById(R.id.userUsername);
            userAvatar = itemView.findViewById(R.id.userAvatar);
        }
    }
}
