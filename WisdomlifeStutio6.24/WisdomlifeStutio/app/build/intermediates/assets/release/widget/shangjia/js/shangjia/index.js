var urId;
var page = 1;
var pageCount = 1;
var lon;
var lat;
var map;
apiready = function() {
	if (api.systemType == 'ios') {
		var cc = $api.dom('.xiaobiao');
		$api.css(cc, 'margin-top:0px;');
	}
	//		var tel = $api.byId('tel');
	//		$api.css(tel, 'padding-right:25px;');
	//	}
	//	FileUtils.readFile("info.json", function(info, err) {
	//				urId=info.userNo;
	//
	//			});
	//	$('#tab1').click(function() {
	//			api.openWin({
	//			name : 'seller',
	//			url : '../busdetail/seller.html',
	//			animation : {
	//				type : "push", //动画类型（详见动画类型常量）
	//				subType : "from_right", //动画子类型（详见动画子类型常量）
	//				duration : 300 //动画过渡时间，默认300毫秒
	//			}
	//		});
	//	});
	//	$('#tab2').click(function() {
	//			api.openWin({
	//			name : 'seller',
	//			url : '../busdetail/seller.html',
	//			animation : {
	//				type : "push", //动画类型（详见动画类型常量）
	//				subType : "from_right", //动画子类型（详见动画子类型常量）
	//				duration : 300 //动画过渡时间，默认300毫秒
	//			}
	//		});
	//	});
	//	$('#tab3').click(function() {
	//			api.openWin({
	//			name : 'seller',
	//			url : '../busdetail/seller.html',
	//			animation : {
	//				type : "push", //动画类型（详见动画类型常量）
	//				subType : "from_right", //动画子类型（详见动画子类型常量）
	//				duration : 300 //动画过渡时间，默认300毫秒
	//			}
	//		});
	//	});

	FileUtils.readFile("info.json", function(info, err) {
		urId = info.userNo;
		total(urId);
		Ytotal(urId);

	});
	var isSjFirst = api.getPrefs({
		sync : true,
		key : 'isSjFirst'
	});
	//加载蒙版
	if (isSjFirst == "YES") {
		$(".tankuang_box").show();
		$(".black_box").show();
		api.setPrefs({
			key : 'isSjFirst',
			value : 'NO'
		});
	}

	//初始加载蒙版
	$('.sjclose').click(function(){
		$('.tankuang_box').hide();
		$('.black_box').hide();
	});
	$('.sjimg').click(function(){
		api.openWin({
				name : 'zadange.html',
				url : '../../html/wallet/zadange.html',
				animation : {
					type : "push", //动画类型（详见动画类型常量）
					subType : "from_right", //动画子类型（详见动画子类型常量）
					duration : 300 //动画过渡时间，默认300毫秒
				}
			});
	});
	//榨汁机
	$('#juicing').click(function(){
		api.openWin({
				name : 'juicing.html',
				url : 'juicing.html',
				animation : {
					type : "push", //动画类型（详见动画类型常量）
					subType : "from_right", //动画子类型（详见动画子类型常量）
					duration : 300 //动画过渡时间，默认300毫秒
				}
			});
	});
	//门禁
	$('#entranceGuard').click(function(){
		api.openWin({
				name : 'entranceGuard.html',
				url : 'entranceGuard.html',
				animation : {
					type : "push", //动画类型（详见动画类型常量）
					subType : "from_right", //动画子类型（详见动画子类型常量）
					duration : 300 //动画过渡时间，默认300毫秒
				}
			});
	});
	//牙刷
	$('#toothbrush').click(function(){
		api.openWin({
				name : 'toothbrush.html',
				url : 'toothbrush.html',
				animation : {
					type : "push", //动画类型（详见动画类型常量）
					subType : "from_right", //动画子类型（详见动画子类型常量）
					duration : 300 //动画过渡时间，默认300毫秒
				}
			});
	});
	//牙刷
	$('#lock').click(function(){
		api.openWin({
				name : 'lock',
				url : 'lock.html',
				animation : {
					type : "push", //动画类型（详见动画类型常量）
					subType : "from_right", //动画子类型（详见动画子类型常量）
					duration : 300 //动画过渡时间，默认300毫秒
				}
			});
	});
	//获得商品信息分类列表
	function list() {
		AjaxUtil.exeScript({
			script : "mobile.business.business",
			needTrascation : true,
			funName : "findCompanyType",
			//	      form:{
			//	         userNo:urId
			//	      },
			success : function(data) {
				console.log("商品详情" + $api.jsonToStr(data));
				if (data.formDataset.checked == 'true') {
					var account = data.formDataset.companyType;
					var list = $api.strToJson(account);
					var nowli = "";
					var nowlis = "";
					for (var i = 0; i < list.length; i++) {
						//	       				 nowli += "<div class='swiper-slide'><span id="+list[i].typeflag+">"+list[i].typename+"</span></div>";
						if (i < 10) {
							nowli = '<span id="' + list[i].fid + '">' + '<img src="' + rootUrl + list[i].picpath + '"></img>' + '<div class="leibie">' + list[i].typename + '</div>' + '</span>';
							$('#showTypeInfo').append(nowli);
						} else {
							nowlis = '<span id="' + list[i].fid + '">' + '<img src="' + rootUrl + list[i].picpath + '"></img>' + '<div class="leibie">' + list[i].typename + '</div>' + '</span>';
							$('#showTypeInfo1').append(nowlis);
						}

					}
					//	       				$('#showTypeInfo').append(nowli);
				} else {
//					alert(data.formDataset.errorMsg);
				}
			}
		});
	}

	list();

	//推荐商家列表
	function recommendList() {
		AjaxUtil.exeScript({
			script : "mobile.business.business",
			needTrascation : true,
			funName : "findBusinessIsrecommendList",
			form : {
				is_recommend : '0',
				is_online : '0',
				recordCount : 6,
				lng : '',
				lat : '',
				typename : ''
			},
			success : function(data) {
				console.log("推荐商家" + $api.jsonToStr(data));
				if (data.formDataset.checked == 'true') {
					var account = data.formDataset.companyDataList;
					var list = $api.strToJson(account);

					if (list.length == undefined || list.length == 0 || list == undefined || list == '' || list.length == '') {
						//	       				api.toast({
						//	                       msg:'暂无商家记录'
						//                     });
						return false;
					} else {
						var nowli = "";
						for (var i = 0; i < list.length; i++) {
							nowli += '<dl class="recommend-details" id="' + list[i].fid + '" data="' + list[i].companytype + '"><img src="' + rootUrl + list[i].shopurl + '" alt="" /><dt>' + list[i].companyname + '</dt><dd>' + list[i].companytype + '</dd></dl>';

						}
						$('#recommend').append(nowli);
					}
				} else {
//					alert(data.formDataset.errorMsg);
				}
			}
		});
	}

	recommendList();

	$('#sou').click(function() {
		$('#tab1').html('');
		page = 1;
		getDistance("",""); 
	});

	//
	//	api.addEventListener({
	//		name : 'keydown'
	//	}, function(ret, err) {
	//		alert(123);
	//		if(keyCode==84){
	//			$('#tab1').html('');
	//			page=1;
	//			businessList(1);
	//		}
	//	});
	//  软键盘的搜索按钮
	//	$('#search').keydown(function(e){
	//		alert(e.keyCode);
	//		if(e.keyCode==13){
	//			$('#tab1').html('');
	//			page=1;
	//			businessList(1);
	//		}
	//	})

	//	单击相应的分类获取分类
	$('#showTypeInfo').on('click', 'span', function() {
		var typeId = this.id;
		//			alert(typeId);
		$('#tab1').children().remove();
		getDistance(typeId,"");
	});

	$('#showTypeInfo1').on('click', 'span', function() {
		var typeId = this.id;
		$('#tab1').children().remove();
		getDistance(typeId,""); 
	});

	//		$('#search').focus(function(){
	//			setTimeout(function(){
	//				api.toast({
	//	            msg:'亲,请点击左侧放大镜来进行搜索哦！'
	//          });
	//			},500)
	//
	//		})

	//	function topList(title){
	//		AjaxUtil.exeScript({
	//	      script:"mobile.business.business",
	//	      needTrascation:true,
	//	      funName:"findCompanyType",
	//	      form:{
	//	         title:title
	//	      },
	//	      success:function (data) {
	//	      console.log($api.jsonToStr(data));
	//	       if (data.formDataset.checked == 'true') {
	//	       		var account = data.formDataset.recordList;
	//	       		console.log('account为'+account);
	//	       		var list=$api.strToJson(account);
	//	       		console.log('list为'+list);
	//	       			var nowli ="";
	//	       			var nowli='<div class="box" id="'+list.fid+'">'
	//	       					+'<i class="icon iconfont icon-xiangyou1" style=" font-size:15px;color: #cccccc;float: right"></i>'
	//	       					+'<div class="left">'
	//	       					+'<img src="'+rootUrl+list.shopurl+'" alt="" />'
	//							+'</div>'
	//							+'<div class="right">'
	//							+'<span id="sjname">'+list.companyname+'</span>'
	//							+'<div id="sjaddress">'+list.address+'</div>'
	//							+'<ul style="margin-bottom: 2px">'
	//							+'<li class="other">'+list.companytype+'</li>'
	//							+'</ul>'
	//							+'</div>'
	//							+'</div>';
	//					$('#tab1').html("");
	//	       			$('#tab1').append(nowli);
	//	         } else {
	//	             alert(data.formDataset.errorMsg);
	//	         }
	//	       }
	//	    });
	//	}

	//金银蛋交易总额
	function total(urId) {
		AjaxUtil.exeScript({
			script : "mobile.business.business",
			needTrascation : true,
			funName : "allEgg",
			form : {
				userNo : urId
			},

			success : function(data) {
				if (data.formDataset.checked == 'true') {
					var account = data.formDataset.recordList;
					var list = $api.strToJson(account);
					//	       		console.log('list为'+list);

					for (var i = 0; i < list.length; i++) {
						if (list[i].rebatetype == '1') {
							$('#totalG').html(list[i].deal_amount);
						} else if (list[i].rebatetype == '2') {
							$('#totalS').html(list[i].deal_amount);
						}
					}

				} else {
//					alert(data.formDataset.errorMsg);
				}
			}
		});
	}

	//金银蛋昨夜交易总额
	function Ytotal() {
		AjaxUtil.exeScript({
			script : "mobile.business.business",
			needTrascation : true,
			funName : "queryYesterdayEgg",
			form : {
				userNo : urId
			},
			success : function(data) {
				console.log($api.jsonToStr(data));
				if (data.formDataset.checked == 'true') {
					var account = data.formDataset.recordList;
					var list = $api.strToJson(account);

					for (var i = 0; i < list.length; i++) {

						if (list[i].rebatetype == '1') {
							$('#ytotalG').html(list[i].deal_amount);
							//	       					alert(list[i].deal_amount);
						} else if (list[i].rebatetype == '2') {
							$('#ytotaly').html(list[i].deal_amount);
						}
					}
				} else {
//					alert(data.formDataset.errorMsg);
				}
			}
		});
	}

	//;

	function getDistance(typeId,city) {
		map = api.require('bMap');
		//api.showProgress();
		map.getLocation({
			accuracy : '10m',
			autoStop : true,
			filter : 1
		}, function(ret, err) {
			//api.hideProgress();
			if (ret.status) {
				lon = ret.lon;
				lat = ret.lat;
				businessList(1, typeId, city);
			} else {
//				alert(err.code);
			}
		});

	}

	getDistance("","");

	//展示商家列表
	function businessList(pages, typeId, city) {
		if(typeof(city) == "undefined"||city==""){
			city = "北京";
		}
		console.log("输出城市信息："+city);
		AjaxUtil.exeScript({
			script : "mobile.business.business",
			needTrascation : true,
			funName : "findBusinessList",
			async : false,
			form : {
				p : pages,
				companytype : typeId || '',
				title : $('#search').val(),
				//	         is_online:0,
				lng : lon,
				lat : lat,
				city: city
			},
			success : function(data) {
				console.log($api.jsonToStr(data));
				if (data.formDataset.checked == 'true') {
					var account = data.formDataset.companyDataList;
					var list = $api.strToJson(account);

					if (list.length == undefined || list.length == 0 || list == undefined || list == '' || list.length == '') {
						if (pages == 1) {
							api.toast({
								msg : '暂无商家记录'
							});
						} else {
							api.toast({
								msg : '无更多商家记录'
							});
						}

					} else {
						for (var i = 0; i < list.length; i++) {
							lon = list[i].longtitude;
							lat = list[i].latitude;
							var starlen = list[i].star;
							//alert(picLen.length); 评星数
							function starLenght(starlen) {
								var daF = '';
								var kf = '';
								if (starlen == 0 || starlen == '' || starlen == undefined) {
									daF = '评星暂无'
								} else {
									for (var k = 0; k < starlen; k++) {
										daF += '<img src="../image/stares.jpg" alt=""/>'
									}
									for (var w = 0; w < 5 - starlen; w++) {
										kf += '<img src="../image/stares.png" alt=""/>'
									}
								}
								return daF + kf;
							}

							if (list[i].address == null || list[i].address == '') {
								list[i].address = '无';
							}
							if (list[i].companytype == null || list[i].companytype == '') {
								list[i].companytype = '无';
							}
							var distance = list[i].distance;
							if (0 < distance && distance < 1000) {
								distance = ('距此' + (distance.toFixed(1)) + 'm');
							} else if (1000 < distance && distance < 5000) {
								distance = ((distance / 1000).toFixed(1));
								distance = ('距此' + distance + 'km');
							} else if (distance > 5000) {
								distance = ('距此大于5km');
							} else {
								distance = ('距离暂无');
							}
							var nowli = '<div class="businessman-box" id="' + list[i].fid + '" data="' + list[i].companytype + '">' + '<div class="businessman-list">' + '<div class="left"><img src="' + rootUrl + list[i].shopurl + '" alt=""/></div>' + '<dl class="left">' + '<dt>' + list[i].companyname + '</dt>' + '<dd>' + '' + starLenght(starlen) + '' + '<span></span>' + '</dd>' + '<dd>' + list[i].companytype + '<span class="text-right" >' + distance + '</span></dd>' + '</dl>' + '</div>' + '</div>';

							$('#tab1').append(nowli);
						}
						//		       		getDistance()
					}

					pageCount = data.formDataset.count > 10 ? Math.ceil(data.formDataset.count / 10) : 1;
					console.log("返回的:pageCount=" + pageCount);
					console.log("返回的page=" + page);
				} else {
//					alert(data.formDataset.errorMsg);
				}
			}
		});
	}

	//businessList(1);

	api.addEventListener({
		name : 'scrolltobottom'
	}, function(ret, err) {
		if (parseInt(page) <= parseInt(pageCount)) {
			page++;
			businessList(page);
		} else {
			page = parseInt(pageCount) + 1;
		}
	});

	//商家列表进行跳转
	$('#tab1').on('click', '.businessman-box', function() {

		api.openWin({//详情界面
			name : 'business-man-list',
			url : '../../sjDetail/business-man-list.html',
			slidBackEnabled : true,
			animation : {
				type : "push", //动画类型（详见动画类型常量）
				subType : "from_right", //动画子类型（详见动画子类型常量）
				duration : 300 //动画过渡时间，默认300毫秒
			},
			pageParam : {
				id : $(this).attr('id'),
				companytype : $(this).attr('data')
			}
		});
	});
	//推荐商家进行跳转
	$('#recommend').on('click', '.recommend-details', function() {

		api.openWin({//详情界面
			name : 'business-man-list',
			url : '../../sjDetail/business-man-list.html',
			slidBackEnabled : true,
			animation : {
				type : "push", //动画类型（详见动画类型常量）
				subType : "from_right", //动画子类型（详见动画子类型常量）
				duration : 300 //动画过渡时间，默认300毫秒
			},
			pageParam : {
				id : $(this).attr('id'),
				companytype : $(this).attr('data')
			}
		});
	});

	//	function getDistance() {
	//		  AjaxUtil.exeScript({
	//		        script:"mobile.business.business",
	//		        needTrascation:false,
	//		        funName:"getDistance",
	//		        form:{
	//		            lng:lon,
	//		            lat:lat
	//		        },
	//		        success:function(data){
	//		            if(data.execStatus == 'true'){
	//		                //alert('获得距离成功！');
	//		                var rows = data.datasources[0].rows;
	//		               // alert(rows.length);
	//		                for (var i = 0; i < rows.length; i++) {
	//		                	var distance=rows[i].distance;
	//		                	//alert(distance);
	//			                if(0<=distance&&distance<1000){
	//				    		$('#keyId'+i+'').html('距此'+distance+'m');
	//
	//						    	}else if(1000<=distance&&distance<5000){
	//						    		var distances=((distance/1000).toFixed(1));
	//						    		$('#keyId'+i+'').html('距此'+distances+'km');
	//
	//						    	}else if(distance>5000){
	//						    		$('#keyId'+i+'').html('距此大于5km');
	//
	//						    	}else {
	//						    		$('#keyId'+i+'').html('暂无');
	//
	//						    	}
	//
	//		               //  $('#keyId'+i+'').html(distance);
	//		                }
	//		            }else {
	//		               // alert("获得距离失败！");
	//		            }
	//		        }
	//		    });
	//}

	//获取商家地理位置
	$('#nearby').click(function() {
		getCityList();
	})
	function getCityList() {
		var hh = 0;
		var UICityList = api.require('UICityList');
		//			if (api.systemType == 'ios') {
		//				hh = 20;
		//			}
		UICityList.open({
			rect : {
				x : 0,
				y : hh,
				w : api.frameWidth,
				h : api.frameHeight
			},
			resource : 'widget://res/UICityList.json',
			styles : {
				searchBar : {
					bgColor : '#f6f6f6',
					cancelColor : '#E3E3E3'
				},
				location : {
					color : '#696969',
					size : 12
				},
				sectionTitle : {
					bgColor : '#eee',
					color : '#000',
					size : 12
				},
				item : {
					bgColor : '#fff',
					activeBgColor : '#696969',
					color : '#000',
					size : 14,
					height : 40
				},
				indicator : {
					bgColor : '#fff',
					color : '#696969'
				}
			},
			currentCity : '北京',
			locationWay : 'GPS(当前定位)',
			hotTitle : '热门城市',
			fixedOn : api.frameName,
			placeholder : '请输入城市'
		}, function(ret, err) {
			if (ret) {
				if (ret.eventType == 'show') {

				} else {
					//						UICityList.close();
					if (ret.eventType == 'selected') {
						//alert(JSON.stringify(ret));
						//alert(JSON.stringify(ret.cityInfo.city));
						//							pageParam : {
						//								id : $(this).attr()
						//							}
						$('#tab1').children().remove();
						getDistance("",ret.cityInfo.city);
						$('#showCity').html(ret.cityInfo.city);
						UICityList.close();
					}
				}

			} else {
				api.alert({
					msg : JSON.stringify(err)
				});
			}
		});
	}

}
