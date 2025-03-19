package com.rick.notes.adapters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.rick.notes.R;
import com.rick.notes.activites.SaveAndCheckPass;
import com.rick.notes.entities.Note;
import com.rick.notes.listeners.NotesListener;
import com.rick.notes.security.MergeSecurity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {

    private List<Note> notes;
    private final List<Note> notesSource;
    private final NotesListener notesListener;
    private static Context ct;
    private Timer timer;

    public NotesAdapter(List<Note> notes, NotesListener notesListener, Context ctt) {
        this.notes = notes;
        this.notesListener = notesListener;
        notesSource = notes;
        this.ct = ctt;
    }
    public List<Note> getNoteData(){
        return notesSource;
    }
    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_container_note, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        holder.setNote(notes.get(position));
        String a = notes.get(position).getTitle();
        holder.layoutNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    //SaveAndCheckPass-------------------
                    SaveAndCheckPass check =  new SaveAndCheckPass(ct.getApplicationContext());
                    check.saveEditIndex(a);
                    //Toast.makeText(ct.getApplicationContext(), check.getEditIndex(), Toast.LENGTH_SHORT).show();
                    notesListener.onNoteClicked(notes.get(adapterPosition), adapterPosition);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return notes.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {

        TextView textTitle, textSubtitle, textDateTime;
        LinearLayout layoutNote;
        RoundedImageView imageNote;
        RatingBar rateNote;
        NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.textTitle);
            textSubtitle = itemView.findViewById(R.id.textSubtitle);
            textDateTime = itemView.findViewById(R.id.textDateTime);
            layoutNote = itemView.findViewById(R.id.layoutNote);
            imageNote = itemView.findViewById(R.id.imageNote);
            //rateNote = itemView.findViewById(R.id.ratingBar);
        }

        void setNote(Note note) {
            SaveAndCheckPass save = new SaveAndCheckPass(ct);
            MergeSecurity decode = new MergeSecurity();///xoa diiii
            //textTitle.setText(decode.decryptData(save.getPassword(),note.getTitle()));

            textTitle.setText(note.getTitle());

            //rateNote.setRating(4.0f);

            if (note.getSubtitle().trim().isEmpty()) {
                textSubtitle.setVisibility(View.GONE);
            } else {
                textSubtitle.setText(note.getSubtitle());
            }
            textDateTime.setText(note.getDateTime());

            GradientDrawable gradientDrawable = (GradientDrawable) layoutNote.getBackground();
            if (note.getColor() != null) {
                gradientDrawable.setColor(Color.parseColor(note.getColor()));
            } else {
                gradientDrawable.setColor(Color.parseColor("#008080"));
            }

            if (note.getImagePath() != null) {
                imageNote.setImageBitmap(BitmapFactory.decodeFile(note.getImagePath()));
                imageNote.setVisibility(View.VISIBLE);
            } else {
                imageNote.setVisibility(View.GONE);
            }
        }
    }

    public void searchNotes(final String searchKeyword,final boolean check1, final boolean check2) {
        timer = new Timer();
        //SaveAndCheckPass save = new SaveAndCheckPass(ct);
        //MergeSecurity decode = new MergeSecurity();
//        int sz = notesSource.size();
//        String aaa = "data: "+sz;
        //Toast.makeText(ct, notesSource.get(2).getNoteText(), Toast.LENGTH_SHORT).show();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (searchKeyword.trim().isEmpty()) {
                    notes = notesSource;
                } else {

                    ArrayList<Note> temp = new ArrayList<>();
                    //Log.d("thaiiiiiiiii",save.getPassword());
                    //Toast.makeText(NotesAdapter.ct, save.getPassword(), Toast.LENGTH_SHORT).show();
                    for (Note note : notesSource) {
                        if(check1 == true && check2 == true){
                            if (note.getTitle().toLowerCase().contains(searchKeyword.toLowerCase()) ||
                                     note.getSubtitle().toLowerCase().contains(searchKeyword.toLowerCase()) || note.getNoteText().toLowerCase().contains(searchKeyword.toLowerCase())
                            ) {
                                temp.add(note);
                            }
                        } else if (check1 == false && check2 == true) {
                            if ( note.getNoteText().toLowerCase().contains(searchKeyword.toLowerCase())
                            ) {
                                temp.add(note);
                            }
                        } else if (check1 == true && check2 == false) {
                            if ( note.getTitle().toLowerCase().contains(searchKeyword.toLowerCase()) ||
                                     note.getSubtitle().toLowerCase().contains(searchKeyword.toLowerCase())
                            ) {
                                temp.add(note);
                            }
                        } else if (check1 == false && check2 == false) {
                                //temp.add(note);n
                            break;
                        }

                    }
                    notes = temp;
                }

                new Handler(Looper.getMainLooper()).post(() -> notifyDataSetChanged());
            }
        }, 500);
    }

    public void cancelTimer() {
        if (timer != null) {
            timer.cancel();
        }
    }
}
