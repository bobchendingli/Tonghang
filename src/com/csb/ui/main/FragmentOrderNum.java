package com.csb.ui.main;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.app.LauncherActivity.ListItem;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.csb.R;
import com.csb.adapter.ImagePagerAdapter;
import com.csb.ui.view.autoscrollviewpager.AutoScrollViewPager;
import com.csb.ui.view.dropdownlist.DropDownListView;
import com.csb.ui.view.dropdownlist.DropDownListView.OnDropDownListener;
import com.csb.utils.ToastUtils;

/**
 * 订阅号
 * 
 * @author bobo
 * 
 */
public class FragmentOrderNum extends Fragment {
	private AutoScrollViewPager viewPager;
	private TextView indexText;
	private List<Integer> imageIdList;
	
	private static LinkedList<Map<String, Object>>   listItems           = null;
    private DropDownListView     listView            = null;
    private EfficientAdapter adapter;
    public static final int      MORE_DATA_MAX_COUNT = 3;
    public int                   moreDataCount       = 0;
    
    private Context context = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_ordernum, container,
				false);
		context = view.getContext();
		viewPager = (AutoScrollViewPager) view.findViewById(R.id.view_pager);
		indexText = (TextView) view.findViewById(R.id.view_pager_index);

		imageIdList = new ArrayList<Integer>();
		imageIdList.add(R.drawable.banner1);
		imageIdList.add(R.drawable.banner2);
		imageIdList.add(R.drawable.banner3);
		viewPager.setAdapter(new ImagePagerAdapter(context,
				imageIdList));
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
		indexText.setText(new StringBuilder().append("1/").append(
				imageIdList.size()));

		viewPager.setInterval(2000);
		viewPager.setCycle(true);
		viewPager.startAutoScroll();

		// the more properties whose you can set
		// // set whether stop auto scroll when touching, default is true
		// viewPager.setStopScrollWhenTouch(false);
		// // set whether automatic cycle when auto scroll reaching the last or
		// first item
		// // default is true
		// viewPager.setCycle(false);
		// /** set auto scroll direction, default is AutoScrollViewPager#RIGHT
		// **/
		// viewPager.setDirection(AutoScrollViewPager.LEFT);
		// // set how to process when sliding at the last or first item
		// // default is AutoScrollViewPager#SLIDE_BORDER_NONE
		// viewPager.setBorderProcessWhenSlide(AutoScrollViewPager.SLIDE_BORDER_CYCLE);
		// viewPager.setScrollDurationFactor(3);
		// viewPager.setBorderAnimation(false);
		
		
		listView = (DropDownListView)view.findViewById(R.id.list_view);
        // set drop down listener
        listView.setOnDropDownListener(new OnDropDownListener() {

            @Override
            public void onDropDown() {
//                new GetDataTask(true).execute();
            }
        });

        // set on bottom listener
        listView.setOnBottomListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
//                new GetDataTask(false).execute();
            }
        });
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            	if(position < 9) {
            		Intent intent = new Intent(context, XueYuanDetailActivity.class);
            		Map<String, Object> map = listItems.get(position);
            		Bundle bundle = new Bundle();
            		bundle.putString("title", map.get("title").toString());
            		bundle.putString("context", map.get("context").toString());
            		intent.putExtras(bundle);
            		context.startActivity(intent);
            		
            	} else if(position ==9){
            		ToastUtils.show(context, "没有新的通知");
            	}
            }
        });
        // listView.setShowFooterWhenNoMore(true);

        listItems = new LinkedList<Map<String, Object>>();
        Map<String, Object> m1 = new HashMap<String, Object>();
        m1.put("title", "中欧冠脉介入学院");
        m1.put("context","    中欧冠脉介入学院由陈韵岱教授担任院长，并由吕树铮教授、陈纪言教授 、傅向华教授、王伟民教授 、颜红兵教授、葛雷教授、张抒扬教授和高炜教授担纲课程设计及传授国内介入领域技术和前沿进展。课程设置着重于介绍冠脉介入治疗的技术，分为基本课程、临床提升课程和学术进展课程，旨在使心血管专科医生对冠脉介入治疗的基础内容有全面掌握，同时了解临床复杂病变的介入治疗及最新进展，提升自身水平。\n\n    在线必修课程内容涉及冠状动脉解剖及病变、造影技术、介入治疗的适应证及指南解读、指引导管的选择及操作、指引导丝的选择及应用、球囊和支架的选择及应用，对较复杂的左主干分叉病变、钙化病变及慢性闭塞病变的介入治疗以及旋磨技术的应用也将展开讨论，此外还将就支架内再狭窄机制及治疗进展、生物可降解支架和围术期抗栓药物的应用进展进行专题讲授。课程内容将定期进行更新，针对国内外的进展及焦点问题进行专题讨论，提升我国心血管介入医生的理论水平。");
        m1.put("icon", context.getResources().getDrawable(R.drawable.img_xy1));
        listItems.add(m1);
        
        Map<String, Object> m2 = new HashMap<String, Object>();
        m2.put("title", "中欧动脉硬化学院");
        m2.put("context","    中欧动脉硬化学院是在中国医师协会领导下的一个虚体单位。该学院的组织构架由学院院长、学院教委会、学院讲师及学员组成。目前学院院长由在该领域有丰富经验的陈红教授担任。学院教委会由本领域中国资深专家及ESC专家共同组成，包括著名的张运院士、赵水平教授、李建军教授、叶平教授、殷伟贤教授、陆国平教授、纪立农教授、武剑教授和沈晨阳教授，此外全国各地的优秀青年讲师30余名组成学院讲师团。\n\n    该学院承担对心血管医生进行动脉粥样硬化危险因素及动脉粥样硬化疾病防治继续教育的任务，教育内容将涵盖心血管内科、神经内科、内分泌科、血管外科等相关学科，内容涉及广泛。我们将继续完善和使用传统的教学方法，同时将在网络信息平台探讨使用现代技术来提高培训的广度和深度，并加快知识传播速度。");
        m2.put("icon", context.getResources().getDrawable(R.drawable.img_xy2));
        listItems.add(m2);
        
        Map<String, Object> m3 = new HashMap<String, Object>();
        m3.put("title", "中欧冠心病学院");
        m3.put("context","    中欧冠心病学院旨在为我国心血管领域各层级临床医师提供继续学习和发展的平台。学院将汇集国内外顶级权威专家学者，组成强大的专家委员会，通过丰富的学术活动，分享和交流冠心病临床诊疗规范管理、技术推广、专科医师培训、科研组织等方面的高质量学术资源和专家经验，并将与心血管专科医师准入和定期考核相衔接，这不仅会推动我国冠心病诊疗规范、推广和提高及开展高水平的临床科研，更有利于为中国广大患者提供更优质的医疗服务。");
        m3.put("icon", context.getResources().getDrawable(R.drawable.img_xy3));
        listItems.add(m3);
        
        Map<String, Object> m4 = new HashMap<String, Object>();
        m4.put("title", "中欧血压学院");
        m4.put("context","    血压学院由孙宁玲教授担任院长，课程设置以临床应用为主，兼顾基础及学术进展，旨在使心血管专科医生能更好地掌握高血压及其靶器官损害的诊断和治疗。在线必修课程中，基本课程包括高血压的流行情况、定义、病因、发病机制、风险评估、诊断和鉴别诊断；临床应用课程则重点介绍高血压及其心脏损害、脑卒中、肥胖、糖尿病和慢性肾病以及难治性高血压、高血压急症和亚急症的诊断与治疗（包括非药物治疗和合理的药物治疗）；学术进展则针对目前国际权威的高血压指南（美国指南、欧洲指南及加拿大指南）与中国临床实践的情况进行专题讨论，以期将国外的权威指南与中国的临床实践更好地结合。");
        m4.put("icon", context.getResources().getDrawable(R.drawable.img_xy4));
        listItems.add(m4);
        
        Map<String, Object> m5 = new HashMap<String, Object>();
        m5.put("title", "中欧心血管影像学院");
        m5.put("context","    “中欧心血管学院项目”是“中国心血管医师专科教育项目”的子项目，是由中国医师协会（CMDA）主办，、中华医学会心血管病学分会（CSC）、中国医师协会心血管内科医师分会(CCCP)、中华医学会心电生理和起搏分会（CSPE）和欧洲心血管学会（ESC）联合提供学术支持，整合了中欧心血管领域丰富的学术资源。\n\n    旨在搭建各层级心血管医生终身学习平台，提供心血管医师定期考核的配套教育课程，促进医师专科准入能力的提升，进而提高中国心血管医师队伍的整体素质，从而更好地服务于中国心血管病患者，同时，推动中国心血管病治疗和研究水平跻身世界前列。");
        m5.put("icon", context.getResources().getDrawable(R.drawable.img_xy5));
        listItems.add(m5);
        
        Map<String, Object> m6 = new HashMap<String, Object>();
        m6.put("title", "中欧抗凝学院");
        m6.put("context","    “中欧心血管学院项目”是“中国心血管医师专科教育项目”的子项目，是由中国医师协会（CMDA）主办，、中华医学会心血管病学分会（CSC）、中国医师协会心血管内科医师分会(CCCP)、中华医学会心电生理和起搏分会（CSPE）和欧洲心血管学会（ESC）联合提供学术支持，整合了中欧心血管领域丰富的学术资源。\n\n    旨在搭建各层级心血管医生终身学习平台，提供心血管医师定期考核的配套教育课程，促进医师专科准入能力的提升，进而提高中国心血管医师队伍的整体素质，从而更好地服务于中国心血管病患者，同时，推动中国心血管病治疗和研究水平跻身世界前列。");
        m6.put("icon", context.getResources().getDrawable(R.drawable.img_xy6));
        listItems.add(m6);
        
        Map<String, Object> m7 = new HashMap<String, Object>();
        m7.put("title", "基本功");
        m7.put("context","    这是为心血管内科医师专门设计的基本功训练单元，目的是帮助心血管医生锻炼和提高临床基本技能。项目将定期发布基于专家提供的临床诊疗基本功素材（包括心电图、超声心动图、心音、心电图、心肌标志物、X光片、动态血压等）的临床小测试，您可通过这些测试巩固和提升临床专业技能，不仅能更好的迎接定期考核，更能更好的为患者服务。\n\n    此外，项目也将发布医学英语小测验，您将听到来自心血管专业会议上的国外讲者演讲音频片断，通过翻译来锻炼自己的专业英语能力，为将来更多的参与国际学习和交流打好基础。同时，在小测试中表现优异的同道将获得参加项目更丰富活动的邀请。");
        m7.put("icon", context.getResources().getDrawable(R.drawable.img_xy7));
        listItems.add(m7);
        
        Map<String, Object> m8 = new HashMap<String, Object>();
        m8.put("title", "大师观点");
        m8.put("context","    心血管病领域的进展和发现日新月异，每天都在更新我们对疾病本质和治疗手段的认识。同时，来自不同研究者的观点和证据又往往互相矛盾，是否采信这些信息、如何采信和指导我们的临床与研究，是每个临床医师经常面对的难题。\n\n    我们将邀请中欧专家提出他们认为反映当前热点的问题，交由广大临床医师来投票选出自己想听的题目，再由专家们就这些题目进行阐述和辩论。\n\n    大师观点——从您的关注点出发，大师们带您深入学术热点的内核！");
        m8.put("icon", context.getResources().getDrawable(R.drawable.img_xy8));
        listItems.add(m8);
        
        Map<String, Object> m9 = new HashMap<String, Object>();
        m9.put("title", "Journal Club");
        m9.put("context","    每周、每日都有很多新的文献、新的研究发表，一天不更新，就怕自己被滚滚前进的学术车轮拉下？ \n\n    全球文献数量惊人，数不胜数，内容又良莠不齐，繁忙工作之余，哪还有时间篇篇研读，去芜存菁？\n\n    我们将邀请心血管领域众多有影响力的专家，在本领域的国际核心期刊中，每周筛选对国内临床实践有指导或借鉴意义的文献，以中文摘要及语音解读的形式推荐给您。\n\n    您每周只需抽出几分钟时间，就可以掌握学界最可信的前沿动态，始终和国际前沿同步！");
        m9.put("icon", context.getResources().getDrawable(R.drawable.img_xy9));
        listItems.add(m9);
        
        Map<String, Object> m10 = new HashMap<String, Object>();
        m10.put("title", "通知");
        m10.put("icon", context.getResources().getDrawable(R.drawable.img_xy10));
        listItems.add(m10);
        
//        listItems.addAll(Arrays.asList(mStrings));
        adapter = new EfficientAdapter(context);
        listView.setAdapter(adapter);
		return view;
	}

	@Override
	public void onPause() {
		super.onPause();
		// stop auto scroll when onPause
		viewPager.stopAutoScroll();
	}

	@Override
	public void onResume() {
		super.onResume();
		// start auto scroll when onResume
		viewPager.startAutoScroll();
	}

	public class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageSelected(int position) {
			indexText.setText(new StringBuilder().append(position + 1)
					.append("/").append(imageIdList.size()));
		}

		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}
	
	
	private class GetDataTask extends AsyncTask<Void, Void, LinkedList<Map<String, Object>>> {

        private boolean isDropDown;

        public GetDataTask(boolean isDropDown){
            this.isDropDown = isDropDown;
        }

        @Override
        protected LinkedList<Map<String, Object>> doInBackground(Void... params) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                ;
            }
            return listItems;
        }

        @Override
        protected void onPostExecute(LinkedList<Map<String, Object>> result) {

            if (isDropDown) {
            	 Map<String, Object> m1 = new HashMap<String, Object>();
                 m1.put("title", "高压学院top");
                 m1.put("icon", context.getResources().getDrawable(R.drawable.banner3));
                 
                listItems.addFirst(m1);
                adapter.notifyDataSetChanged();

                // should call onDropDownComplete function of DropDownListView at end of drop down complete.
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd HH:mm:ss");
                listView.onDropDownComplete(getString(R.string.update_at) + dateFormat.format(new Date()));
            } else {
                moreDataCount++;
                Map<String, Object> m1 = new HashMap<String, Object>();
                m1.put("title", "高压学院bottom");
                m1.put("icon", context.getResources().getDrawable(R.drawable.banner1));
                listItems.add(m1);
                adapter.notifyDataSetChanged();

                if (moreDataCount >= MORE_DATA_MAX_COUNT) {
                    listView.setHasMore(false);
                }

                // should call onBottomComplete function of DropDownListView at end of on bottom complete.
                listView.onBottomComplete();
            }

            super.onPostExecute(result);
        }
    }
	
	private static class EfficientAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
//        private Bitmap mIcon1;
//        private Bitmap mIcon2;

        public EfficientAdapter(Context context) {
            // Cache the LayoutInflate to avoid asking for a new one each time.
            mInflater = LayoutInflater.from(context);

            // Icons bound to the rows.
//            mIcon1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.abs__ic_go);
//            mIcon2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon48x48_2);
        }

        /**
         * The number of items in the list is determined by the number of speeches
         * in our array.
         *
         * @see android.widget.ListAdapter#getCount()
         */
        public int getCount() {
            return listItems.size();
        }

        /**
         * Since the data comes from an array, just returning the index is
         * sufficent to get at the data. If we were using a more complex data
         * structure, we would return whatever object represents one row in the
         * list.
         *
         * @see android.widget.ListAdapter#getItem(int)
         */
        public Object getItem(int position) {
            return position;
        }

        /**
         * Use the array index as a unique id.
         *
         * @see android.widget.ListAdapter#getItemId(int)
         */
        public long getItemId(int position) {
            return position;
        }

        /**
         * Make a view to hold each row.
         *
         * @see android.widget.ListAdapter#getView(int, android.view.View,
         *      android.view.ViewGroup)
         */
        public View getView(int position, View convertView, ViewGroup parent) {
            // A ViewHolder keeps references to children views to avoid unneccessary calls
            // to findViewById() on each row.
            ViewHolder holder;

            // When convertView is not null, we can reuse it directly, there is no need
            // to reinflate it. We only inflate a new View when the convertView supplied
            // by ListView is null.
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.listview_item_ordernum, null);

                // Creates a ViewHolder and store references to the two children views
                // we want to bind data to.
                holder = new ViewHolder();
                holder.title = (TextView) convertView.findViewById(R.id.item_title_tv);
//                holder.detail = (TextView) convertView.findViewById(R.id.item_detail_tv);
                holder.icon = (ImageView) convertView.findViewById(R.id.item_logo_iv);

                convertView.setTag(holder);
            } else {
                // Get the ViewHolder back to get fast access to the TextView
                // and the ImageView.
                holder = (ViewHolder) convertView.getTag();
            }

            // Bind the data efficiently with the holder.
            Map<String, Object> item = listItems.get(position);
            holder.title.setText(item.get("title").toString());
//            holder.detail.setText(item.get("detail").toString());
            holder.icon.setBackgroundDrawable((Drawable) item.get("icon"));

            return convertView;
        }

        static class ViewHolder {
            TextView title;
//            TextView detail;
            ImageView icon;
        }
    }
}
