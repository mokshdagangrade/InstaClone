package com.example.instaclone.Frames;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instaclone.MainActivity;
import com.example.instaclone.R;
import com.example.instaclone.adapters.SearchAdapter;
import com.example.instaclone.models.User;
import com.example.instaclone.utils.utility;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    SearchView searchView;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseStorage firebaseStorage;

    View mView;
    View _rootView=null;

    utility Utility;

    RecyclerView recyclerView;
    private List<User> userList;
    private SearchAdapter adapter;

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(_rootView==null) {
            mView = inflater.inflate(R.layout.fragment_search, container, false);
            _rootView=mView;
            searchView = mView.findViewById(R.id.searchView);
            recyclerView = mView.findViewById(R.id.search_recyclerView);

            userList = new ArrayList<>();
            adapter = new SearchAdapter(userList);

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), ((LinearLayoutManager) layoutManager).getOrientation());
            recyclerView.addItemDecoration(dividerItemDecoration);

            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);


            mAuth = FirebaseAuth.getInstance();
            db = FirebaseFirestore.getInstance();
            firebaseStorage = FirebaseStorage.getInstance();

            Utility = new utility();


            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(final String s) {

                    // do query when u click submit
                    db.collection("USERS")
                            .orderBy("username")
                            .limit(25)
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    userList.clear();
                                    adapter.notifyItemRangeRemoved(0,userList.size()-1);
                                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                                        User temp = (documentSnapshot.toObject(User.class));
                                        if (temp.getName().toLowerCase().matches(".*" + s.toLowerCase() + ".*") || temp.getUsername().toLowerCase().matches(".*" + s.toLowerCase() + ".*")) {
                                            userList.add(temp);
                                        }

                                    }
                                    adapter.notifyDataSetChanged();


                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Utility.makeToast(MainActivity.mainContext, "Failed to load data...");
                                }
                            });


                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    return false;
                }
            });

        }
        else{

        }


        return _rootView;
    }

    @Override
    public void onDestroyView() {
        if(_rootView.getParent()!=null)
        {
            ((ViewGroup)_rootView.getParent()).removeView(_rootView);
        }
        super.onDestroyView();
    }
}

