package com.mycompany.sonatafinance.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.mycompany.sonatafinance.R;
import com.mycompany.sonatafinance.activities.MainActivity;
import com.mycompany.sonatafinance.database.Category;
import com.mycompany.sonatafinance.database.DaoNotes;
import com.mycompany.sonatafinance.database.Note;
import com.mycompany.sonatafinance.database.SoNataDatabaseOpenHelper;
import java.util.ArrayList;
import java.util.Objects;

import static androidx.core.content.PermissionChecker.checkPermission;


public class CreateNoteFragment extends Fragment {

  public static final String TAG = "CreateNoteFragment";

  public interface OnReadyButtonListener {
    void onReady();

  }


  Category category = null;
  boolean isRecordingMicrophone = false;
  SpeechRecognizer speechRecognizer;
  EditText editText;
  ImageView ivIconCategory;
  FrameLayout frameLayout;
  FrameLayout recordingMicrophone;
  Note note;
  boolean isUpdate = false;

  OnReadyButtonListener onReadyButtonListener;



  @Override public void onAttach(@NonNull Context context) {
    onReadyButtonListener = (MainActivity) context;
    super.onAttach(context);
  }

  public CreateNoteFragment() {
  }

  @Nullable @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_note_create, container, false);

    //add pack pressed arrow in action bar
    ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
    if (actionBar != null) {
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(androidx.appcompat.R.drawable.abc_ic_ab_back_material);
    }

    requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(),
        new OnBackPressedCallback( true ) {
          @Override public void handleOnBackPressed() {
            showDialogConfirmation();
          }
        });

    editText = view.findViewById(R.id.editTextText);
    ImageView ivPicture = view.findViewById(R.id.iv_todo_picture);
    frameLayout = view.findViewById(R.id.iv_pick_category);
    ivIconCategory = view.findViewById(R.id.iv_icon_category);
    recordingMicrophone = view.findViewById(R.id.frame_layout_recording_microfone);
    Button btReady = view.findViewById(R.id.button);
    initSpeechRecognizer();

    GradientDrawable drawable = new GradientDrawable();

    recordingMicrophone.setOnClickListener(v -> {
      if(isRecordingMicrophone) {
        stopSpeechRecognizer();
        recordingMicrophone.setBackground(getResources().getDrawable(R.drawable.shape_circul));
        isRecordingMicrophone = false;
      } else {
        startSpeechRecognizer();
        recordingMicrophone.setBackground(getResources().getDrawable(R.drawable.shape_circul_record));
        isRecordingMicrophone = true;
      }
    });


    if (note != null) {
      editText.setText(note.getContent());
      } else {
      if(!isUpdate) {
        category = Category.getDefaultCategory();
      }
    }
    drawable.setCornerRadius(20);
    drawable.setColor(category.getColor());
    frameLayout.setBackground(drawable);
    ivIconCategory.setImageResource(getResources().getIdentifier(category.getIcon(), "drawable", getContext().getPackageName()));


    ivIconCategory.setOnClickListener(v -> {

      FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
      transaction.addToBackStack(null);
      PickCategoryFragment pickCategoryFragment = new PickCategoryFragment();
      pickCategoryFragment.setOnCategorySelectedListener(category -> {
        this.category = category;
        isUpdate = true;
      });

      transaction.replace(R.id.fragmentContainerView, pickCategoryFragment, TAG).commit();
    });

    btReady.setOnClickListener(v -> {
      if (note == null) {
        note = new Note(editText.getText().toString(), category);
        new DaoNotes(getContext()).insert(note);
      } else {
        note.setContent(editText.getText().toString());
        note.setCategory(category);
        new DaoNotes(getContext()).update(note);
      }
      onReadyButtonListener.onReady();
    });

    return view;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);
  }

  @Override public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
    inflater.inflate(R.menu.menu_delete_note, menu);
    super.onCreateOptionsMenu(menu, inflater);
  }

  @Override public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      showDialogConfirmation();
      return true;
    } else if(item.getItemId() == R.id.pick_note_item_delete) {
      if(note != null) {
        showDialogConfirmationDelete();
      }
    }
    return super.onOptionsItemSelected(item);
  }

  public void setNote(Note note) {
    if(note != null){
      this.note = note;
      this.category = note.getCategory();
    }
  }

  void initSpeechRecognizer() {
    if (SpeechRecognizer.isRecognitionAvailable(getContext())) {
      speechRecognizer = SpeechRecognizer.createSpeechRecognizer(getContext());
      speechRecognizer.setRecognitionListener(new RecognitionListener() {
        @Override public void onReadyForSpeech(Bundle bundle) {

        }

        @Override public void onBeginningOfSpeech() {

        }

        @Override public void onRmsChanged(float v) {

        }

        @Override public void onBufferReceived(byte[] bytes) {

        }

        @Override public void onEndOfSpeech() {

        }

        @Override
        public void onError(int i) {
          /*String error;
          switch (i) {
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
              error = "ERROR_INSUFFICIENT_PERMISSIONS";
              break;
            case SpeechRecognizer.ERROR_NETWORK:
              error = "ERROR_NETWORK";
              break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
              error = "ERROR_NETWORK_TIMEOUT";
              break;
            case SpeechRecognizer.ERROR_NO_MATCH:
              error = "ERROR_NO_MATCH";
              break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
              error = "ERROR_RECOGNIZER_BUSY";
              break;
            case SpeechRecognizer.ERROR_SERVER:
              error = "ERROR_SERVER";
              break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
              error = "ERROR_SPEECH_TIMEOUT";
              break;
            default:
              error = "ERROR_UNKNOWN";
              break;
          }
          Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();*/
          recordingMicrophone.setBackground(getResources().getDrawable(R.drawable.shape_circul));
          isRecordingMicrophone = false;
        }

        @Override public void onResults(Bundle bundle) {
          ArrayList<String> matches =
              bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
          if (matches != null && !matches.isEmpty()) {
            StringBuilder recognizedText = new StringBuilder();
            for (String match : matches) {
              recognizedText.append(match);
            }
            //установить проверку, есть ли текст перед записью
            if (editText.getText().length() > 0) {
              editText.append(". " + recognizedText.toString());
            } else {
              editText.setText(recognizedText.toString());
            }

          }
          if(isRecordingMicrophone){
            startSpeechRecognizer();
          }
        }

        @Override public void onPartialResults(Bundle bundle) {

        }

        @Override public void onEvent(int i, Bundle bundle) {

        }
      });
    } else {
      Toast.makeText(getContext(), "Speech recognition is not available", Toast.LENGTH_SHORT)
          .show();
    }
  }

  void startSpeechRecognizer() {

    if(ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
      ActivityCompat.requestPermissions((Activity)getContext(), new String[]{android.Manifest.permission.RECORD_AUDIO}, 1);
    } else {
      Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
      intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
          RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
      intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ru");
      intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Говорите");

      if (speechRecognizer != null) {
        speechRecognizer.startListening(intent);
        recordingMicrophone.setBackground(getResources().getDrawable(R.drawable.shape_circul_record));
        //Toast.makeText(getContext(), "start", Toast.LENGTH_SHORT).show();
      }
    }
  }

  void stopSpeechRecognizer() {
    if (speechRecognizer != null) {
      speechRecognizer.stopListening();
      //Toast.makeText(getContext(), "stop", Toast.LENGTH_SHORT).show();
    }
  }


  private void showDialogConfirmation() {
    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
    builder.setTitle("Заметка не сохранена");
    builder.setMessage("Вы действительно хотите выйти?");
    builder.setPositiveButton("Да", (dialog, which) -> {
      onReadyButtonListener.onReady();
    });
    builder.setNegativeButton("Нет", (dialog, which) -> {});
    builder.show();
  }

  private void showDialogConfirmationDelete() {
    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
    builder.setTitle("Удалить заметку?");
    builder.setMessage("Вы действительно хотите удалить заметку?");
    builder.setPositiveButton("Да", (dialog, which) -> {
      new DaoNotes(getContext()).delete(note);
      onReadyButtonListener.onReady();
    });
    builder.setNegativeButton("Нет", (dialog, which) -> {});
    builder.show();
  }

}
