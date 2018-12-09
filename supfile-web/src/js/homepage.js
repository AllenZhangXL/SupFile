
var i=0;
jummper();
function jummper(){
  $(".pic ul li").eq(i).find("img").css("left","-1180px");
  $(".pic ul li").eq(i).find("p").css("width","0px");
  $(".pic ul li").eq(i).find("img").animate({left:"0px"},500,function(){
    //当图片移动完成后再加载进度条
    //alert("当图片移动完成后再做操作");
    $(".pic ul li").eq(i).find("p").animate({width:"1174px"},8000,function(){
      $(".pic ul li").eq(i).find("img").animate({left:"1180px"},500,function(){
        i++;
        if(i>2)
        i=0;
        $(".pic ul li").eq(i).fadeIn(100).siblings().fadeOut(100);
      });
    });
  });
}
setInterval("jummper()",9100);
