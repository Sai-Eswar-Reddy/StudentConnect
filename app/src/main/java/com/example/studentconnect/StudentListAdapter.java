package com.example.studentconnect;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StudentListAdapter extends RecyclerView.Adapter<StudentListAdapter.StudentViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Student student);
    }

    private List<Student> studentList;
    private Context context;
    private boolean isAdmin;
    private String loggedInEmail;
    private OnItemClickListener listener;

    public StudentListAdapter(List<Student> studentList, Context context, boolean isAdmin, String loggedInEmail) {
        this.studentList = studentList;
        this.context = context;
        this.isAdmin = isAdmin;
        this.loggedInEmail = loggedInEmail;
    }

    public void setStudents(List<Student> students) {
        this.studentList = students;
        notifyDataSetChanged();
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public void setLoggedInEmail(String loggedInEmail) {
        this.loggedInEmail = loggedInEmail;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_student, parent, false);
        return new StudentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        Student student = studentList.get(position);
        holder.fullNameTextView.setText(student.getFullName());
        holder.branchTextView.setText(student.getBranch() + " - " + student.getSection());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(student);
                } else {
                    Intent intent = new Intent(context, StudentDetailsActivity.class);
                    intent.putExtra("fullName", student.getFullName());
                    intent.putExtra("rollNo", student.getRollNo());
                    intent.putExtra("email", student.getEmail());
                    intent.putExtra("branch", student.getBranch());
                    intent.putExtra("section", student.getSection());
                    intent.putExtra("year", student.getYear());
                    intent.putExtra("phoneNo", student.getPhoneNo());
                    intent.putExtra("isAdmin", isAdmin);
                    intent.putExtra("loggedInEmail", loggedInEmail);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public static class StudentViewHolder extends RecyclerView.ViewHolder {
        public TextView fullNameTextView;
        public TextView branchTextView;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            fullNameTextView = itemView.findViewById(R.id.textViewFullName);
            branchTextView = itemView.findViewById(R.id.textViewBranch);
        }
    }
}