

var spider_url = '123.57.54.83';

var panelstr = '<div class="hook_panel_inner"><div class="hook_tag_group hook_tag_group_single"><h3>情绪类型：</h3><div><span class="hook_tag">高兴</span><span class="hook_tag">调皮</span><span class="hook_tag">尴尬</span><span class="hook_tag">惊奇</span><span class="hook_tag">愤怒</span><span class="hook_tag">无聊</span><span class="hook_tag">害怕</span><span class="hook_tag">厌恶</span><span class="hook_tag">大哭</span><span class="hook_tag">忧愁</span><span class="hook_tag">感动</span></div></div><div class="hook_tag_group hook_tag_group_multiple"><h3>场景类型：</h3><div><span class="hook_tag">卖萌</span><span class="hook_tag">自拍</span><span class="hook_tag">得意炫耀</span><span class="hook_tag">赞扬</span><span class="hook_tag">狂欢</span><span class="hook_tag">闺蜜基友</span><span class="hook_tag">羡慕想要</span><span class="hook_tag">幸运</span><span class="hook_tag">生日纪念日</span><span class="hook_tag">体育比赛</span><span class="hook_tag">求助帮忙</span><span class="hook_tag">祈祷</span><span class="hook_tag">花痴</span><span class="hook_tag">告别</span><span class="hook_tag">归来</span><span class="hook_tag">累</span><span class="hook_tag">减肥</span><span class="hook_tag">睡觉</span><span class="hook_tag">早起</span><span class="hook_tag">失眠</span><span class="hook_tag">生病</span><span class="hook_tag">倒霉</span><span class="hook_tag">遇到难题</span><span class="hook_tag">大餐吃饭</span><span class="hook_tag">被忽视</span><span class="hook_tag">遇到奇葩</span><span class="hook_tag">使坏</span><span class="hook_tag">冷</span><span class="hook_tag">热</span><span class="hook_tag">醉了</span><span class="hook_tag">时间伤逝</span><span class="hook_tag">青春梦想</span></div></div><div class="hook_tag_group hook_tag_group_multiple"><h3>典型事物：</h3><div><span class="hook_tag">星座</span><span class="hook_tag">猫</span><span class="hook_tag">狗</span><span class="hook_tag">超人</span><span class="hook_tag">动漫</span><span class="hook_tag">爱情</span><span class="hook_tag">音乐</span><span class="hook_tag">文艺</span></div></div><div class="hook_tag_group hook_tag_group_single"><h3>情绪强度：</h3><div><span class="hook_tag">强烈</span><span class="hook_tag">温和</span></div></div><div class="hook_tag_group hook_tag_group_single"><h3>典型对象：</h3><div><span class="hook_tag">男性</span><span class="hook_tag">女性</span></div></div></div><div class="hook_panel_expander">X</div>';

var bd = $('body').on('click', function(e){
	e.stopPropagation();
	e.preventDefault();
	var tar = $(e.target);
	var item = $(tar.closest('.card9'));


	if(item.length == 0) { return  false}


	if(item.hasClass("feed_list_item_hook")) {
		item.removeClass("feed_list_item_hook")
	} else {
		if(item.hasClass("feed_list_item_lock")) {
			alert("这篇微博已提交!")
		} else {
			item.addClass("feed_list_item_hook")
		}
	};

	var panel = item.find('.hook_panel');


	if(panel.length == 0 ) {
		var panel = $("<div>").addClass("hook_panel").html(panelstr);
		panel.on('click', function(e){
			e.stopPropagation();
			e.preventDefault();
			return false;
		});

		item.append(panel);

		var hook_tags = panel.find('.hook_tag');
		var expander = panel.find('.hook_panel_expander').on('click', function(e){
			panel.toggleClass("hook_panel_fold");
		});

		// panel.on('dblclick', function(e){
		// 	expander.click();
		// });


		$('.hook_tag_group_single', panel).on('click', function(e){
			var tar = $(e.target), tag = $(tar.closest(".hook_tag"));
			if(tag.length == 0 ) { 
				expander.click();
				return;
			}

			if(tag.hasClass("hook_tag_selected")) {
				tag.removeClass("hook_tag_selected");
			} else {
				$(this).find(".hook_tag").removeClass("hook_tag_selected");
				tag.addClass("hook_tag_selected");
			}
		});
		$('.hook_tag_group_multiple', panel).on('click', function(e){
			var tar = $(e.target), tag = $(tar.closest(".hook_tag"));
			if(tag.length == 0 ) {
				expander.click();
				return;
			}
			tag.toggleClass("hook_tag_selected");
		});					
	} else {
		panel.removeClass("hook_panel_fold");
	}
});

var submitBtn = $('<button>').html("上传").addClass("hook_submit_btn").on('click', function(){
	var hooked_items = $('.feed_list_item_hook');
	var data = [];
	if(hooked_items.length == 0) {return}

	for(var i=0, l=hooked_items.length; i<l; i++) {
		var item = $(hooked_items[i]);

		var tags = item.find(".hook_tag_selected");
		tags = Array.prototype.map.call(tags, function(tag){
			return $(tag).text();
		});

		if(tags.length == 0) {
			alert("有选中的文本未标记！");
			return
		}
		var text = item.find(".default-content").text();
		
		var authE = item.find('header a.item-main'), 
			author = authE[0]?[authE.attr("href")]:[];

		var datum = {author: author, text: text, tags: tags};
		data.push(datum);
	}


	$.ajax({
	    url: 'http://' + spider_url + ':5000/send',
	    method: 'POST', 
	    contentType: 'application/json',
	    data: JSON.stringify(data),
	    success: function(res) {
	    	hooked_items.addClass("feed_list_item_lock").removeClass("feed_list_item_hook");
	    	alert('提交成功！');
	    }
	});

}).appendTo(bd);


function isEmpty() {
	for(var i =0, l = arguments.length; i<l; i++) {
		var arg = arguments[i];
		if(arg != "0") {return false}
	}
	return true
}

