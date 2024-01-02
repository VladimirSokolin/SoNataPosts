package com.mycompany.sonatafinance.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.FileUtils;
import android.os.ParcelFileDescriptor;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.font.otf.Glyph;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.colorspace.PdfColorSpace;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.IBlockElement;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.mycompany.sonatafinance.R;
import com.mycompany.sonatafinance.database.DaoNotes;
import com.mycompany.sonatafinance.database.Note;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class PrintFragment extends Fragment {

  private static final int CREATE_PDF_REQUEST_CODE = 42;
  String filePath;
  ArrayList<Note> notes;
  public PrintFragment(){}

  @Nullable @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View root = inflater.inflate(R.layout.fragment_print, container, false);
    Button btPrint = root.findViewById(R.id.bt_print);


    btPrint.setOnClickListener(v -> {
      printNotes();
    });

    return root;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    notes = new DaoNotes(getContext()).getAll();
  }

  private void printNotes(){
    createPdf();

    /*if(filePath != null){
      try {
        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(filePath));
        Document document = new Document(pdfDocument);

        for(Note note : notes){
          document.add(new Paragraph("Date: " + note.getDate()));
          document.add(new Paragraph("Category: " + note.getCategoryName()));
          document.add(new Paragraph("Content: " + note.getContent()));
          document.add(new AreaBreak());
        }
        document.close();
      } catch (Exception e) {
        e.printStackTrace();
      }

    }*/
  }

  private void createPdf(){
    Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
    intent.addCategory(Intent.CATEGORY_OPENABLE);
    intent.setType("application/pdf");
    intent.putExtra(Intent.EXTRA_TITLE, "notes.pdf");
    startActivityForResult(intent, CREATE_PDF_REQUEST_CODE);
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if(requestCode == CREATE_PDF_REQUEST_CODE && resultCode == getActivity().RESULT_OK){
      if(data != null && data.getData() != null){
        Uri uri = data.getData();
        createPdfFromNotes(uri);
        //filePath = uri.getPath();
      }
    }
  }

  @SuppressLint("NewApi") private void createPdfFromNotes(Uri uri) {
    try {
      ParcelFileDescriptor pfd = requireContext().getContentResolver().openFileDescriptor(uri, "w");

      FileOutputStream fos = new FileOutputStream(pfd.getFileDescriptor());

      PdfWriter writer = new PdfWriter(fos);
      PdfDocument pdf = new PdfDocument(new PdfWriter(writer));
      Document document = new Document(pdf);

      document.setFont(setFont());
      document.setMargins(20, 20, 1, 20);
      SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy  HH:mm", Locale.getDefault());
      for(Note note : notes){
        document.add(new Paragraph(sdf.format(note.getDate())).setBold().setMarginTop(30));
        document.add(new Paragraph(note.getCategoryName() + "\n" + note.getContent()));
      }
      document.close();

      /*StringBuilder sb = new StringBuilder();

      for(Note note : notes){
        sb.append(sdf.format(note.getDate()) + "\n");
        sb.append(note.getCategoryName() + "\n");
        sb.append(note.getContent() + "\n\n");
      }
      document.add(new Paragraph(sb.toString()));
      document.close();*/


      Toast.makeText(requireContext(), "PDF создан", Toast.LENGTH_SHORT).show();
    } catch (IOException e) {
      e.printStackTrace();
      // Обработка ошибок записи PDF-файла
      Toast.makeText(requireContext(), "Ошибка создания PDF: " + e.getMessage(), Toast.LENGTH_SHORT).show();
    }
  }

  private PdfFont setFont(){
    AssetManager assetManager = getContext().getAssets();
    InputStream inputStream = null;
    try {
      inputStream = assetManager.open("font_garamond.otf");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    // Чтение данных файла шрифта в массив байтов
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    byte[] buffer = new byte[1024];
    int length;
    while (true) {
      try {
        if (!((length = inputStream.read(buffer)) != -1)) break;
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      outputStream.write(buffer, 0, length);
    }
    byte[] fontData = outputStream.toByteArray();

    // Создание шрифта из массива байтов

    FontProgram fontProgram = null;
    try {
      fontProgram = FontProgramFactory.createFont(fontData);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    // Создание PdfFont из FontProgram
    return PdfFontFactory.createFont(fontProgram, PdfEncodings.IDENTITY_H, true);
  }

  private Color createColor(int color){

    // Извлечение составляющих красного, зеленого и синего цветов
    int red = (color >> 16) & 0xFF;
    int green = (color >> 8) & 0xFF;
    int blue = color & 0xFF;
    return new DeviceRgb(red, green, blue);
  }
}
