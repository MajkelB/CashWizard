package eu.pp.cashwizard;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
//import android.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemLongClick;
import butterknife.OnItemSelected;
import butterknife.OnTextChanged;
import eu.pp.cashwizard.configuration.Conf;
import eu.pp.cashwizard.data.DataRepository;
import eu.pp.cashwizard.dict.Action;
import eu.pp.cashwizard.dict.AnswerType;
import eu.pp.cashwizard.dict.Sex;
import eu.pp.cashwizard.model.Person;
import eu.pp.cashwizard.util.AUtil;
import eu.pp.cashwizard.util.JUtil;
import eu.pp.cashwizard.view.adapters.other.DialogCallback;
import eu.pp.cashwizard.view.adapters.other.PickDateCallback;
import eu.pp.cashwizard.view.adapters.other.ViewCaller;

public class PersonDetailsActivity extends AppCompatActivity implements PickDateCallback, DialogCallback {

    @BindView( R.id.personDetailsNickName ) EditText eNickName;
    @BindView( R.id.personDetailsFirstName ) EditText eFirstName;
    @BindView( R.id.personDetailsLastName ) EditText eLastName;
    @BindView( R.id.personDetailsBirthDate ) EditText eDateBirth;
    @BindView( R.id.personDetailsSex ) Spinner sSex;
    @BindView( R.id.personDetailsImage ) ImageView imPhoto;

    @BindView( R.id.bPersonDetailsCancel ) ImageButton bCancel;
    //@BindView( R.id.bPersonDetailsDelete) ImageButton bDelete;


    private static final int CAMERA_REQUEST = 10;
    private static final int PICTURE_CROP = 11;
    private static final int PICTURE_PICK = 22;

    private static final int COMM_CHANGE_PHOTO = 100;
    private static final int COMM_TAKE_NEW_PHOTO = 101;
    private static final int COMM_CROP_PHOTO = 102;
    private static final int COMM_CONFIRM_DETELE = 103;



    //    private ImageView imageView;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    Person person;
    Long personId;
    ArrayAdapter<String> sexAdapter;

    int PHOTO_WIDTH;
    int PHOTO_HEIGHT;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_details);
        ButterKnife.bind( this );
        init();
    }

    private void init() {
//        getSupportActionBar().hide();
        //this.getActionBar().hide();
//        List<Bill> bills = JUtil.safeList( DataRepository.getBills() );
//        items = new ArrayList<>();
//        for( Bill b: bills ) items.add( new BillsListRow( b ) );
//
//        peopleAdapter = new BillsListAdapter(this, items );
//        lPeopleList.setAdapter( peopleAdapter );
//        lPeopleList.setOnItemClickListener( this );
//
        View backgroundImage = findViewById(R.id.personDetailsBody);
        Drawable background = backgroundImage.getBackground();
        background.setAlpha(80);
        Bundle extras = getIntent().getExtras();

        PHOTO_WIDTH = Conf.getIntegerProperty( "photo.profile.width" );
        PHOTO_HEIGHT = Conf.getIntegerProperty( "photo.profile.height" );

        sexAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, Sex.values() );
        sSex.setAdapter( sexAdapter );
        sSex.setSelection( 0 );

        if (extras != null) {
            personId = Long.parseLong( extras.getString("PersonId") );
            AUtil.logI( "Person to edit: " + personId );
            person = DataRepository.getPersonById( personId );
            //The key argument here must match that used in the other activity

        }
        if( person == null ) {
            Toast.makeText( this, "Person not found - create new!", Toast.LENGTH_LONG).show();
            person = Person.newPerson();
//            setResult( RESULT_CANCELED );
//            PersonDetailsActivity.this.finish();
        } // else {
            eNickName.setText( person.getNickName() );
            eFirstName.setText( person.getFirstName() );
            eLastName.setText( person.getLastName() );
            sSex.setSelection( Sex.getPosition( person.getSex() ));
            eDateBirth.setText( JUtil.getDate( person.getDateBorn() ) );
        //}
        eDateBirth.setOnClickListener( view -> AUtil.showDateCalendar( PersonDetailsActivity.this, PersonDetailsActivity.this, ViewCaller.DEFAULT) );
        eNickName.setEnabled( true );
        imPhoto.setMaxWidth( PHOTO_WIDTH );
        imPhoto.setMaxHeight( PHOTO_HEIGHT );
        imPhoto.setLayoutParams( new LinearLayout.LayoutParams( AUtil.px2DP( this, PHOTO_WIDTH ), AUtil.px2DP( this, PHOTO_HEIGHT ) ) );
        if( person != null && person.hasPhoto() ) {
            imPhoto.setImageBitmap(AUtil.getBitmapFromName( person.getPhotoFullPath() ) );
        }
        eNickName.setOnFocusChangeListener( (v,hasfocus) -> AUtil.hideKeyBoard(v, this, hasfocus) );
        eFirstName.setOnFocusChangeListener( (v,hasfocus) -> AUtil.hideKeyBoard(v, this, hasfocus) );
        eLastName.setOnFocusChangeListener( (v,hasfocus) -> AUtil.hideKeyBoard(v, this, hasfocus) );

//        if( person.getPhotoFileName() != null ) {
//
//            String fileSrc = person.getPhotoFileName();
//            AUtil.logI( "Photo file name: " + fileSrc );
//            Glide.with(this)
//                 .load( Uri.fromFile( AUtil.getFileFromName( fileSrc ) ) )
//                 .into( imPhoto );
//        }

        bCancel.requestFocus();
    }

    @OnClick( R.id.bPersonDetailsDelete )
    public void deletePerson() {
        AUtil.logI( "Delete person?: " );
        AUtil.showYesNoDialog( this, "Confirm", "Do you want to delete person: " + eNickName.getText(), this, COMM_CONFIRM_DETELE );
    }
//    @OnItemLongClick( R.id.personDetailsImage )
//    @OnClick( R.id.bPersonDetailsDelete )
    public void changePhoto() {
//        Toast.makeText(this, "aaa", Toast.LENGTH_LONG).show();
//        testMediaScanner();

        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            //Toast.makeText(this, "bbb", Toast.LENGTH_LONG).show();
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
        }
        else
        {
            //Toast.makeText(this, "ccc", Toast.LENGTH_LONG).show();
            //Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            //startActivityForResult(cameraIntent, CAMERA_REQUEST);
            dispatchTakePictureIntent();
            AUtil.logI( "FilePath: " + currentPhotoPath );
        }
    }


//    @OnClick( R.id.bPersonDetailsEdit )
//    public void editPerson() {
//        eNickName.setEnabled( true );
//
//    }

    @OnClick( R.id.personDetailsImage )
    public void photoClicked() {
        if( person.hasPhoto() ) {
            AUtil.showYesNoDialog( PersonDetailsActivity.this, getString( R.string.dialog_question_label ), getString( R.string.person_details_change_photo_question ), this, COMM_CHANGE_PHOTO);
        } else {
            answer(COMM_CHANGE_PHOTO, AnswerType.YES );
        }

    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        AUtil.logI( "OnActivityResult :) requestCode = " + requestCode );
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK)
        {
            String fileName = person.getNickName()+JUtil.getPhotoFileNameUI()+".jpg";
            Bitmap photo = (Bitmap) data.getExtras().get("data");
//            Glide.with( this )
//                    .load( photo )
//                    .into( imPhoto );

//            Glide.with(this)
//                    .asBitmap()
//                    .load(photo)
//                    .into(new SimpleTarget<Bitmap>() {
//                        @Override
//                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
//                            AUtil.logI( "FileSrc: " + Conf.getPhotoDirectory() + "/" + fileName );
//                            //AUtil.writePhoto( PersonDetailsActivity.this, fileName, resource);
//                            try {
//                                 imageFile = createImageFile();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                            dispatchTakePictureIntent();
//                        }
//                    });
            dispatchTakePictureIntent();
            ///galleryAddPic( currentPhotoPath );




            //imPhoto.setImageBitmap(photo);
        } else if( requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK ) {
            String [] perms = { Manifest.permission.WRITE_EXTERNAL_STORAGE };
            requestPermissions( perms ,1 );
            if ( checkSelfPermission( Manifest.permission.WRITE_EXTERNAL_STORAGE ) == PackageManager.PERMISSION_GRANTED ) {
                AUtil.logI("OnActivityResult będzie zapisywanie");
                galleryAddPic2(currentPhotoPath);
                //Bitmap b = resizeBitmap( currentPhotoPath, 100, 200 );
                //String fileName = getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + person.getNickName()+JUtil.getPhotoFileNameUI()+".jpg";
                //performCrop( );
                photoPicker();
            }
        } else if( requestCode == PICTURE_PICK && resultCode == Activity.RESULT_OK ) {
            AUtil.logI( "Photo picked!!!!!!!!!!");
            Uri photoUri = data.getData();
            if (photoUri != null) {
                AUtil.logI( "Start Crop!!");
                performCrop(photoUri);
            }
        } else if( requestCode == PICTURE_CROP && resultCode == Activity.RESULT_OK ) {
            String fileName = person.getNickName()+JUtil.getPhotoFileNameUI()+".jpg";
            String filePath = Conf.DIRECTORY_SMALL_PICTURES + "/" + fileName;
            Bundle extras = data.getExtras();
            // get the cropped bitmap
            Bitmap b = extras.getParcelable("data");
            imPhoto.setImageBitmap(b);
            person.setPhotoFileName( fileName );
            AUtil.writePhoto( PersonDetailsActivity.this, filePath, b );
            AUtil.logI("OnActivityResult zapisane: " + filePath );
        }
    }



    File imageFile;
    String currentPhotoPath;
    static final int REQUEST_TAKE_PHOTO = 1;
    public MediaScannerConnection msc = null;
    private MediaScannerConnection.MediaScannerConnectionClient mediaScannerConnectionClient =
            new MediaScannerConnection.MediaScannerConnectionClient() {

                @Override
                public void onMediaScannerConnected() {
                    AUtil.logI( "MediaScanner connected" );
                    //File f = new File( "//storage/emulated/0/Android/data/eu.pp.cashwizard/files/DCIM/aaa.jpg" );
//                    File f = new File( "//storage/emulated/0/Download/aaa.jpg" );
                    File f = new File( currentPhotoPath );
                    if( f.exists() ) AUtil.logI( "File exists (CAN READ: " + f.canRead() + "): " + f.getAbsolutePath() );
                    else AUtil.logI( "File not exists" );
                    msc.scanFile(f.getAbsolutePath() , null);
                    AUtil.logI( "MediaScanner scanFile" );
                }

                @Override
                public void onScanCompleted(String path, Uri uri) {
                    AUtil.logI( "SCAN completed: " + path );
                    AUtil.logI( "SCAN completed URI: " + (uri==null?"NULL":uri.getPath()) );
                    if(path.equals(currentPhotoPath ))

                        msc.disconnect();
                        AUtil.logI( "MediaScanner disconnected" );
                }
            };


//    private void galleryAddPic( String fileURI ) {
//        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//        File f = new File( "file:///storage/emulated/0/Android/data/eu.pp.cashwizard/files/Pictures/aaa.jpg" );
//        Uri contentUri = Uri.fromFile(f);
//        AUtil.logI( "galleryAddPic - URI: " + contentUri.getPath() );
//        mediaScanIntent.setData(contentUri);
//        this.sendBroadcast(mediaScanIntent);
//    }



//    private void testMediaScanner() {
//        AUtil.logI( "MediaScanner test" );
//        AUtil.logI( "ExternalFilesDir: " + getExternalFilesDir(Environment.DIRECTORY_PICTURES) );
//        AUtil.logI( "DirectoryDCIM: " + getExternalFilesDir(Environment.DIRECTORY_DCIM) );
//        AUtil.logI( "ExternalStoragePublicDirectory: " + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) );
//        AUtil.logI( "ExternalStoragePublicDirectoryDCIM: " + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) );
//        msc = new MediaScannerConnection(this, mediaScannerConnectionClient);
//        msc.connect();
//
//    }

    @Override
    public void pickDate(Date date, ViewCaller caller) {
        eDateBirth.setText( JUtil.getDate( date ));
    }


//    @Override
//    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//    }


    @OnClick( R.id.bPersonDetailsOk )
    public void okClicked() {
        Person lPerson = null;
        if( person != null ) lPerson = DataRepository.getPersonById( person.getId() );
        Intent data = new Intent();
        data.putExtra( "PersonId", person.getId() );
        if( lPerson != null ) { // update person
            data.putExtra( "Action", Action.UPDATE );
            if( DataRepository.updatePerson(person) ) setResult( RESULT_OK, data );
            else setResult( RESULT_CANCELED, data );
            AUtil.logI( "PersonId poprawione: " + person.getId() );
        } else { // new person
            data.putExtra( "Action", Action.ADD );
            person.setFirstName( eFirstName.getText().toString() );
            person.setLastName( eLastName.getText().toString() );
            person.setNickName( eNickName.getText().toString() );
            person.setDateBorn( JUtil.getDate( eDateBirth.getText().toString() ) );
            person.setSex( (Sex)sSex.getSelectedItem() );
            DataRepository.getPersons().add( person );
            AUtil.logD( "Szczegóły osoby: " + person.getId() + " * " + person.toString() );
            AUtil.logI( "PersonId wstawione: " + person.getId() );
            setResult( RESULT_OK, data );
        }
        PersonDetailsActivity.this.finish();
    }

    @OnClick( R.id.bPersonDetailsCancel )
    public void cancelClicked() {
        Intent data = new Intent();
        data.putExtra( "Action", Action.CANCEL );
        data.putExtra( "PersonId", person.getId() );
        setResult( RESULT_OK, data );
        PersonDetailsActivity.this.finish();

    }

    @OnTextChanged(R.id.personDetailsFirstName)
    void firstNameChanged(CharSequence charSequence ) {
        person.setFirstName( charSequence.toString() );
    }
    @OnTextChanged(R.id.personDetailsLastName)
    void lastNameChanged(CharSequence charSequence ) {
        person.setLastName( charSequence.toString() );
    }
    @OnTextChanged(R.id.personDetailsNickName)
    void nickNameChanged(CharSequence charSequence ) {
        person.setNickName( charSequence.toString() );
    }
    @OnTextChanged(R.id.personDetailsBirthDate)
    void dateBornChanged(CharSequence charSequence ) {
        person.setDateBorn( JUtil.getDate( charSequence.toString() ) );
    }
    @OnItemSelected(R.id.personDetailsSex)
    public void spinnerItemSelected(Spinner spinner, int position) {
        //Currency curr = (Currency) spinner.getSelectedItem();
        Sex s = (Sex) spinner.getItemAtPosition(position);
        person.setSex(s);

    }

    @OnTextChanged(R.id.personDetailsBirthDate)
    void dateChanged(CharSequence charSequence ) {
        Date d = JUtil.getDate( charSequence.toString() );
        if( d != null ) person.setDateBorn( d );
        else {
            AUtil.logI( "Invalid date format: " + charSequence );
            Toast.makeText( this, "Invalid date format!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void answer(int communicationId, AnswerType answerType, String... additionalData) {
        switch ( communicationId ) {
            case COMM_CHANGE_PHOTO:
                if( answerType.isYes() ) {
                    AUtil.showYesNoDialog( PersonDetailsActivity.this, getString( R.string.dialog_question_label ), getString( R.string.person_details_take_new_photo_question ), this, COMM_TAKE_NEW_PHOTO);
                } else if( answerType.isNo() ) {
                } else
                    AUtil.logW( "PersonDetails: wrong answer: " + answerType.name() + "for this communication id: " + communicationId );
                break;
            case COMM_TAKE_NEW_PHOTO:
                if( answerType.isYes() ) {
                    if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        AUtil.logI( "No permissions for CAMERA" );
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                    }
                    else
                        dispatchTakePictureIntent();
                } else if( answerType.isNo() ) {
                    photoPicker();
                } else
                    AUtil.logW( "PersonDetails: wrong answer: " + answerType.name() + "for this communication id: " + communicationId );
                break;
            case COMM_CONFIRM_DETELE:
                if( answerType.isYes() ) {
                    Toast.makeText( this, "Will be deleted", Toast.LENGTH_LONG).show();
                    //DataRepository.removePersonById( person.getId() ); - usuwanie formatke wyzej
                    Intent data = new Intent();
                    data.putExtra( "Action", Action.REMOVE );
                    data.putExtra( "PersonId", person.getId() );
                    setResult( RESULT_OK, data );
                    PersonDetailsActivity.this.finish();
                } else {
                    Toast.makeText( this, "Canceled", Toast.LENGTH_LONG).show();
                }
                break;
            default: AUtil.logW( "PersonDetails: wrong communication id: " + communicationId );



        }
    }


    /************************ PHOTO */

    private void galleryAddPic2( String fileURI ) {
        msc = new MediaScannerConnection(this, mediaScannerConnectionClient);
        msc.connect();
    }

    private void photoPicker() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, PICTURE_PICK);
    }

    public Bitmap resizeBitmap(String photoPath, int targetW, int targetH) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int scaleFactor = 1;
        if ((targetW > 0) || (targetH > 0)) {
            scaleFactor = Math.min(photoW/targetW, photoH/targetH);
        }

        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true; //Deprecated API 21

        return BitmapFactory.decodeFile(photoPath, bmOptions);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(Objects.requireNonNull(getApplicationContext()),
                        BuildConfig.APPLICATION_ID + ".fileprovider", photoFile);
//                Uri photoURI = FileProvider.getUriForFile(this,
//                        "eu.pp.cashwizard",
//                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO );
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private boolean performCrop( Uri photoUri ) {
        // take care of exceptions
        try {
            // call the standard crop action intent (the user device may not support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(photoUri , "image/*");
            // set crop properties
            cropIntent.putExtra("crop", "true");
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 2);
            // indicate output X and Y
            cropIntent.putExtra("outputX", PHOTO_WIDTH );
            cropIntent.putExtra("outputY", PHOTO_HEIGHT );
            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PICTURE_CROP);
            return true;
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            AUtil.logE( "Crop intent not found" );
            Toast.makeText(this, R.string.person_details_no_crop_intent_error, Toast.LENGTH_SHORT).show();

        } catch ( Exception e ) {
            AUtil.logE( "Unexpected exception while calling crop intent", e );
        }
        return false;
    }

//
//    UPDATE 2020 MAR 13
//
//    Provider path for a specific path as followings:
//
//<files-path/> --> Context.getFilesDir()
//<cache-path/> --> Context.getCacheDir()
//<external-path/> --> Environment.getExternalStorageDirectory()
//<external-files-path/> --> Context.getExternalFilesDir(String)
//<external-cache-path/> --> Context.getExternalCacheDir()
//<external-media-path/> --> Context.getExternalMediaDirs()


}
