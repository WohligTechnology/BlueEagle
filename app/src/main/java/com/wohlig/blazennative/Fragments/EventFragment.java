package com.wohlig.blazennative.Fragments;

import android.app.Fragment;

public class EventFragment extends Fragment {

    /*private View view;
    private static Activity activity;
    private static String TAG = "BLAZEN";
    private static ProgressBar progressBar;
    private RecyclerView rvEventList;
    private List<EventListPojo> listEventList;
    private EventListAdapter eventListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_event, container, false);

        ((MainActivity) this.getActivity()).setToolbarText("EVENTS");

        activity = getActivity();

        initilizeViews();

        return view;
    }


    private void initilizeViews() {

        rvVideoAlbum = (RecyclerView) view.findViewById(R.id.rvVideoAlbum);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        listAlbums = new ArrayList<VideoAlbumsPojo>();

        videoAlbumsAdapter = new VideoAlbumsAdapter(listAlbums);
        rvVideoAlbum.setAdapter(videoAlbumsAdapter);

        LinearLayoutManager llm = new LinearLayoutManager(activity);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rvVideoAlbum.setLayoutManager(llm);

        //rvVideoAlbum.addItemDecoration(new SpacesItemDecoration(Size.dpToPx(activity, 10)));

        getContent();
    }

    private void getContent() {

        new AsyncTask<Void, Void, String>() {
            boolean done = false;
            boolean noInternet = false;

            @Override
            protected String doInBackground(Void... params) {

                if (Looper.myLooper() == null) {
                    Looper.prepare();
                }
                String response;

                JSONArray jsonArray;

                try {
                    response = HttpCall.getDataGet(InternetOperations.SERVER_URL + "video/getAllAlbums");
                    if (!response.equals("")) {                 //check is the response empty
                        jsonArray = new JSONArray(response);

                        if (jsonArray.length() > 0) {

                            for (int i = 0; i < jsonArray.length(); i++) {

                                String id = null, image = null, title = null, subTitle = null;

                                JSONObject jsonObject = new JSONObject(jsonArray.get(i).toString());

                                id = jsonObject.optString("id");
                                image = jsonObject.optString("image");
                                title = jsonObject.optString("title");
                                subTitle = jsonObject.optString("subtitle");

                                populateAlbums(id, image, title, subTitle);
                            }
                            done = true;

                        } else {
                            done = true;
                        }
                    } else {                                    //no internet and no cached copy also found in database
                        noInternet = true;
                    }

                } catch (JSONException je) {
                    Log.e(TAG, Log.getStackTraceString(je));
                } catch (Exception e) {
                    Log.e(TAG, Log.getStackTraceString(e));
                }

                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                progressBar.setVisibility(View.GONE);
                if (done) {                         //everything went fine
                    refresh();
                } else if (noInternet) {            //if no internet and no cached copy found in database
                    Toast.makeText(activity, "No internet!", Toast.LENGTH_SHORT).show();
                } else {                            //some error
                    Toast.makeText(activity, "Oops, Something went wrong!", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute(null, null, null);
    }

    public void populateAlbums(String id, String image, String title, String subTitle) {

        VideoAlbumsPojo vap = new VideoAlbumsPojo();
        vap.setId(id);
        vap.setImageUrl(image);
        vap.setTitle(title);
        vap.setSubTitle(subTitle);
        vap.setContext(activity);
        listAlbums.add(vap);
    }

    private void refresh() {
        videoAlbumsAdapter.notifyDataSetChanged();
    }*/
}
