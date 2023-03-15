package ken.example.miniprojects;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.fragment.app.Fragment;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;

import java.util.ArrayList;

/**
 * create an instance of this fragment.
 */
public class CustomerHomeFragement extends Fragment {
    GridView gridView;
    ImageSlider imageSlider;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.customer_fragment_home_fragement, container, false);

        gridView = view.findViewById(R.id.grid_view_layout);
        imageSlider = view.findViewById(R.id.image_slider);


        ArrayList<SlideModel> list = new ArrayList<>();
        list.add(new SlideModel(R.drawable.imgslide1, ScaleTypes.FIT));
        list.add(new SlideModel(R.drawable.imgslider2, ScaleTypes.FIT));

        imageSlider.setImageList(list);

        ArrayList<ServicesModel> serviceList = new ArrayList<>();

        gridView.setAdapter(new ServicesAdapter(serviceList, getActivity()));
        servicesDetails(serviceList);
        return view;

    }
 /*RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        servicesDetails(recyclerView);
        */
/*    SliderView sliderView = view.findViewById(R.id.image_slider);
        // Inflate the layout for this fragment
        mImageSlider(sliderView);
    public void mImageSlider(SliderView sliderView){

        ArrayList<ImageSliderData> imageSliderDataArrayList = new ArrayList<>();
        imageSliderDataArrayList.add(new ImageSliderData("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRjaRYfpEyNyIx_AFqXkQH0oxc6OVDzskCOvg&usqp=CAU"));
        imageSliderDataArrayList.add(new ImageSliderData("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQeILFCe4h0sulnIGTl4yo_w-07qIT2bLKVCQ&usqp=CAU"));
        imageSliderDataArrayList.add(new ImageSliderData("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSKyRHMedgL1vzGBuOM8E9qb_t3XvxArRoINA&usqp=CAU"));
        imageSliderDataArrayList.add(new ImageSliderData("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQMRTYWBtPPM45ocWJuHNq1-xjD_6yYBqJgrQ&usqp=CAU"));

        ImageSliderAdapter imageSliderAdapter = new ImageSliderAdapter(getContext(),imageSliderDataArrayList);
        sliderView.setSliderAdapter(imageSliderAdapter);
        sliderView.startAutoCycle();
        sliderView.setAutoCycle(true);
        sliderView.setScrollTimeInSec(2);
        sliderView.setAutoCycleDirection(sliderView.LAYOUT_DIRECTION_LTR);
    }*/

    public void servicesDetails(ArrayList<ServicesModel> serviceList) {

        serviceList.add(new ServicesModel(R.drawable.plumber, "Plumber"));
        serviceList.add(new ServicesModel(R.drawable.carpenter, "Carpenter"));
        serviceList.add(new ServicesModel(R.drawable.electrician, "Electrician"));
        serviceList.add(new ServicesModel(R.drawable.tutor, "Tutor"));
        serviceList.add(new ServicesModel(R.drawable.painter, "Painter"));
        serviceList.add(new ServicesModel(R.drawable.driver, "Driver"));

    }

}