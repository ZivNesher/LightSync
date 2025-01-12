package com.example.myapplication.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.models.CommandBoundary;

public class CommandAdapter extends RecyclerView.Adapter<CommandAdapter.CommandViewHolder> {

    private final CommandBoundary[] commands;

    public CommandAdapter(CommandBoundary[] commands) {
        this.commands = commands;
    }

    @NonNull
    @Override
    public CommandViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.command_bubble, parent, false);
        return new CommandViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommandViewHolder holder, int position) {
        CommandBoundary command = commands[position];
        holder.commandId.setText("Command ID: " + command.getCommandId().getId());
        holder.commandText.setText("Command: " + command.getCommand());
        holder.targetObject.setText("Target Object: " + command.getTargetObject().getObjectId().getId());
        holder.timestamp.setText("Timestamp: " + command.getInvocationTimestamp());
        holder.invokedBy.setText("Invoked By: " + command.getInvokedBy().getUserId().getEmail());
    }

    @Override
    public int getItemCount() {
        return commands.length;
    }

    public static class CommandViewHolder extends RecyclerView.ViewHolder {
        TextView commandId, commandText, targetObject, timestamp, invokedBy;

        public CommandViewHolder(@NonNull View itemView) {
            super(itemView);
            commandId = itemView.findViewById(R.id.commandId);
            commandText = itemView.findViewById(R.id.commandText);
            targetObject = itemView.findViewById(R.id.targetObject);
            timestamp = itemView.findViewById(R.id.timestamp);
            invokedBy = itemView.findViewById(R.id.invokedBy);
        }
    }
}
