package com.example.salesbuddy.view;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.salesbuddy.R;
import com.example.salesbuddy.model.ReprocessingItem;
import com.example.salesbuddy.view.adapter.ReprocessingAdapter;
import com.example.salesbuddy.view.dialog.MessageDialog;

import java.util.ArrayList;
import java.util.List;

public class ReprocessingActivity extends IncludeToolbar {
    private RecyclerView recyclerView;
    private AppCompatButton btnReprocess;
    private ReprocessingAdapter adapter;
    private List<ReprocessingItem> mockList;
    private boolean alreadyHaveError = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reprocessing);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.rvReprocessing);
        btnReprocess = findViewById(R.id.btnReprocessing);

        createMockData();

        adapter = new ReprocessingAdapter(mockList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        btnReprocess.setOnClickListener(v -> startReprocessingSimulation());
        configToolbar(getString(R.string.reprocess_layout), HomeActivity.class);
    }

    private void createMockData() {
        mockList = new ArrayList<>();
        mockList.add(new ReprocessingItem("Marcos Silva", "R$ 50,00", false));
        mockList.add(new ReprocessingItem("Ana Paula", "R$ 120,00", false));
        mockList.add(new ReprocessingItem("João Souza", "R$ 15,00", false));
        mockList.add(new ReprocessingItem("Maria Clara", "R$ 200,00", false));
        mockList.add(new ReprocessingItem("Pedro Henrique", "R$ 80,00", false));
    }

    private void startReprocessingSimulation() {

        if(!alreadyHaveError){
            MessageDialog.show(ReprocessingActivity.this, getString(R.string.problems_reprocess), null);
            new Handler(Looper.getMainLooper()).postDelayed(() -> {

                if (mockList.size() > 2) {
                    mockList.get(0).setReprocessed(true);
                    mockList.get(2).setReprocessed(true);
                }
                alreadyHaveError = true;
                adapter.notifyDataSetChanged();
            }, 2000);
        } else {
            MessageDialog.show(ReprocessingActivity.this, getString(R.string.sucess_reprocess), null);
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                for (ReprocessingItem item : mockList){
                    item.setReprocessed(true);
                }
                adapter.notifyDataSetChanged();
            }, 2000);
        }
    }
}
