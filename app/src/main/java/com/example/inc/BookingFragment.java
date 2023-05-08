package com.example.inc;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class BookingFragment extends Fragment{
    private TextView dateView;
    private AlertDialog confirmationDialog;


    public BookingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_booking, container, false);

        TextView textViewModel = view.findViewById(R.id.text_view_model);
        TextView textViewMake = view.findViewById(R.id.text_view_model2);
        TextView textViewType = view.findViewById(R.id.text_view_model3);

        // Text input field declarations
        EditText editTextName = view.findViewById(R.id.editTextName);
        EditText editTextContact = view.findViewById(R.id.editTextContact);
        EditText editTextAddress = view.findViewById(R.id.editTextAddress);

        Button submitButton = view.findViewById(R.id.submitButton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"Submitted Placeholder",Toast.LENGTH_SHORT).show();
            }
        });


        // Set the OnEditorActionListener for the first two EditTexts
        editTextName.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                editTextContact.requestFocus();
                return true;
            }
            return false;
        });

        editTextContact.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                editTextAddress.requestFocus();
                return true;
            }
            return false;
        });

        // Handle the "Done" button press for the last EditText
        editTextAddress.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editTextAddress.getWindowToken(), 0);
                return true;
            }
            return false;
        });


        HashMap<String, String[]> makeModelMapping = new HashMap<>();
        makeModelMapping.put("Toyota", new String[]{"Tazz", "Hilux", "Agya", "Corolla", "Land Cruiser", "Fortuner"});
        makeModelMapping.put("Ford", new String[]{"Ranger", "Figo", "Fiesta", "Eco Sport/Kuga"});
        makeModelMapping.put("VW", new String[]{"Polo", "Citi Golf", "Amarok", "T-Cross", "Tiguan"});
        makeModelMapping.put("Hyundai", new String[]{"i20", "Creta", "i10", "Getz"});

        textViewModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.dropdown_dialogs);

                // Set the background for the dialog window
                dialog.getWindow().setBackgroundDrawableResource(R.drawable.rounded_corners_global);

                // Set the dialog title
                TextView dialogTitle = dialog.findViewById(R.id.text_view_model);
                dialogTitle.setText("Select Vehicle Model");

                // Get a reference to the ListView in the dialog layout
                ListView listView = dialog.findViewById(R.id.list_view_models);

                // Create a list of vehicle models
                String[] models = makeModelMapping.keySet().toArray(new String[0]);

                // Set the adapter for the ListView
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, models);
                listView.setAdapter(adapter);

                // Set a click listener on the ListView items
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                        String selectedModel = (String) adapterView.getItemAtPosition(position);

                        // Set the selected item as the text of the TextView
                        TextView textViewModel = getView().findViewById(R.id.text_view_model);
                        textViewModel.setText(selectedModel);
                        // Clear the make TextView and set it to the default text if value changed in model
                        TextView textViewMake = getView().findViewById(R.id.text_view_model2);
                        textViewMake.setText("Select Vehicle Make");

                        // Dismiss the dialog
                        dialog.dismiss();
                    }
                });
                dialog.setCancelable(true);
                dialog.show();
            }
        });


        textViewMake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.dropdown_dialogs);

                // Set the background for the dialog window
                dialog.getWindow().setBackgroundDrawableResource(R.drawable.rounded_corners_global);

                // Set the dialog title
                TextView dialogTitle = dialog.findViewById(R.id.text_view_model);
                dialogTitle.setText("Select Vehicle Make");

                // Get a reference to the ListView in the dialog layout
                ListView listView = dialog.findViewById(R.id.list_view_models);

                // Get the currently selected model
                TextView textViewModel = getView().findViewById(R.id.text_view_model);
                String selectedModel = textViewModel.getText().toString();

                // Get the makes for the selected model
                String[] makes = makeModelMapping.get(selectedModel);

                // Check if there are any makes available for the selected model
                if (makes != null) {
                    // Set the adapter for the ListView
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, makes);
                    listView.setAdapter(adapter);
                } else {
                    // Show a message when there are no makes available for the selected model
                    listView.setVisibility(View.GONE);
                    TextView noMakesMessage = dialog.findViewById(R.id.no_makes_message);
                    noMakesMessage.setVisibility(View.VISIBLE);
                    noMakesMessage.setText("No makes available for the selected model.");
                }

                // Set a click listener on the ListView items
                // Set a click listener on the ListView items
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                        String selectedItem = (String) adapterView.getItemAtPosition(position);

                        // Set the selected item as the text of the TextView
                        TextView textViewMake = getView().findViewById(R.id.text_view_model2);
                        textViewMake.setText(selectedItem);

                        // Dismiss the dialog
                        dialog.dismiss();
                    }
                });

                dialog.setCancelable(true);
                dialog.show();
            }
        });

        textViewType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.dropdown_dialogs);

                // Set the background for the dialog window
                dialog.getWindow().setBackgroundDrawableResource(R.drawable.rounded_corners_global);

                // Set the dialog title
                TextView dialogTitle = dialog.findViewById(R.id.text_view_model);
                dialogTitle.setText("Select Wash Type");

                // Get a reference to the ListView in the dialog layout
                ListView listView = dialog.findViewById(R.id.list_view_models);

                // Create a list of vehicle makes
                String[] types = {"Valet", "Wash n Go", "Clean Inside Only", "Wash Only", "Buff and Polish",
                        "Rims and Tyres", "Rim Shine", "Seat Cleaning and Steaming"};

                // Set the adapter for the ListView
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, types);
                listView.setAdapter(adapter);

                // Set a click listener on the ListView items
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                        String selectedItem = (String) adapterView.getItemAtPosition(position);

                        // Set the selected item as the text of the TextView
                        TextView textViewType = getView().findViewById(R.id.text_view_model3);
                        textViewType.setText(selectedItem);

                        // Dismiss the dialog
                        dialog.dismiss();
                    }
                });

                dialog.setCancelable(true);
                dialog.show();
            }
        });

        dateView = view.findViewById(R.id.dateView);
        dateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker();
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    private void openDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                openTimePicker(year, month, day);
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    private void openTimePicker(final int year, final int month, final int day) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                Calendar selectedDateTime = Calendar.getInstance();
                selectedDateTime.set(year, month, day, hour, minute);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                String dateTimeText = dateFormat.format(selectedDateTime.getTime());
                dateView.setText(dateTimeText);
            }
        }, hour, minute, true);
        timePickerDialog.show();
    }



}
