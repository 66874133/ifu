
<!DOCTYPE html>
<html lang="en-US">
<head>
	
	<link rel="stylesheet" type="text/css" href="/css/test.css" media="all" />
  
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>${title} 	| ${title1}</title>

<script type="text/javascript" src="/js/one.js"></script>
</head>

<body class="archive category category-asmodeus category-5 group-blog">
<div id="page" class="hfeed site">

<div id="scotch-panel">
	<div class="menu-menu-1-container"><ul id="menu-menu-1" class="menu"><li id="menu-item-85" class="menu-item menu-item-type-post_type menu-item-object-page current-menu-item page_item page-item-2 current_page_item menu-item-85"><a href="/index.html">首页</a></li>
     <#list categories as map>
        <li id="menu-item-88" class="menu-item menu-item-type-taxonomy menu-item-object-category menu-item-has-children menu-item-88"><a href="/category/${map['alias']}/">${map['cat']}</a></li>
	 </#list> 
</ul></div></div>

<div class="toggle-panel">
	<i class="fa fa-bars"></i>
</div>

	<header id="masthead" class="site-header" role="banner">
		<div class="container"><div class="row"> 
			<div class="col-sm-4">
				<div class="site-branding">
													<h1 class="site-title logo"><a id="blogname" rel="home" href="index.html" title="blogname">${title}</a></h1>
						
				</div>
			</div>

			<div class="col-sm-8">
				<nav id="site-navigation" class="main-navigation" role="navigation">
					<div class="menu-menu-1-container"><ul id="isomer" class="menu"><li class="menu-item menu-item-type-post_type menu-item-object-page current-menu-item page_item page-item-2 current_page_item menu-item-85"><a href="/index.html">首页</a></li>

<li class="menu-item menu-item-type-taxonomy menu-item-object-category menu-item-has-children menu-item-88"><a href="./category/">分类</a>
<ul class="sub-menu">

     <#list categories as map> 
        <li class="menu-item menu-item-type-taxonomy menu-item-object-category menu-item-89"><a href="/category/${map['alias']}/">${map['cat']}</a></li>
	 </#list> 
</ul>
</li>
<li class="menu-item menu-item-type-taxonomy menu-item-object-category menu-item-has-children menu-item-88"><a href="./tag/">标签</a>
<ul class="sub-menu">
     <#list tags as map> 
        <li class="menu-item menu-item-type-taxonomy menu-item-object-category menu-item-89"><a href="/category/${map['alias']}/">${map['cat']}</a></li>
	 </#list> 
	
</ul>
</li>
<li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-82"><a href="./about/">关于..</a></li>
</ul></div>				</nav><!-- #site-navigation -->
			</div>
		</div></div>
	</header><!-- #masthead -->

	<div id="content" class="site-content">
<header class="title-header">
	<div class="container"><div class="row"> 
		<div class="col-md-12"> 
		<h1>
				${current\.category['cat']}		</h1>
		</div>
	</div></div>
</header><!-- .entry-header -->

<div class="container"><div class="row"> 
	<div class="col-md-8">
	<section id="primary" class="content-area">
		<main id="main" class="site-main" role="main">

				
		 <#list category\.articles as map> 				
				
<article id="post-42" class="post-42 post type-post status-publish format-standard has-post-thumbnail hentry category-arrange category-asmodeus">

				<a href="/article/${map['_id']}.html"> <img class="postimg" src="${map['imgUrlLarge']}" alt="saa" /> </a>
	
	<header class="entry-header">
		<h1 class="entry-title"><a href="/article/${map['_id']}.html" rel="bookmark">${map['articleTitle']}</a></h1>	</header><!-- .entry-header -->

	<div class="row">
		<div class="col-md-4 entry-meta">
			<div> 
				<i class="fa fa-user"></i> ${map['author']}
			</div>
			<div>
				<i class="fa fa-clock-o"></i> ${map['dateTime']}			</div>
			<div>
				<i class="fa fa-tag"></i> 
				
				<#list map['article.categories'] as map2>
				<a href="../${map2['alias']}" rel="category tag">${map2['cat']}</a>, 		
				</#list> 
				</div>
			<!--div>
				 <i class="fa fa-comment"></i> 0 Comments			</div-->
			
		</div>
		<div class="col-md-8">
			<div class="entry-content">
			<p>${map['content']} [&hellip;]</p>
			<a href="/article/${map['_id']}.html" class="readmore"> 更多 </a>
			</div><!-- .entry-content -->
		</div>
	</div>

</article><!-- #post-## -->
 </#list> 
		</main><!-- #main -->
	</section><!-- #primary -->
</div>
<!--
<div class="col-md-4">
	<div id="secondary" class="widget-area" role="complementary">
		<aside id="search-2" class="widget widget_search"><form role="search" method="get" class="search-form" action="http://demo.fabthemes.com/isomer/">
				<label>
					<span class="screen-reader-text">Search for:</span>
					<input type="search" class="search-field" placeholder="Search &hellip;" value="" name="s" />
				</label>
				<input type="submit" class="search-submit" value="Search" />
			</form></aside><aside id="archives-2" class="widget widget_archive"><h1 class="widget-title">Archives</h1>		<ul>
			<li><a href='http://demo.fabthemes.com/isomer/2015/01/'>January 2015</a></li>
	<li><a href='http://demo.fabthemes.com/isomer/2012/06/'>June 2012</a></li>
		</ul>
		</aside><aside id="meta-2" class="widget widget_meta"><h1 class="widget-title">Meta</h1>			<ul>
						<li><a href="http://demo.fabthemes.com/isomer/wp-login.php">Log in</a></li>
			<li><a href="http://demo.fabthemes.com/isomer/feed/">Entries <abbr title="Really Simple Syndication">RSS</abbr></a></li>
			<li><a href="http://demo.fabthemes.com/isomer/comments/feed/">Comments <abbr title="Really Simple Syndication">RSS</abbr></a></li>
			<li><a href="https://wordpress.org/" title="Powered by WordPress, state-of-the-art semantic personal publishing platform.">WordPress.org</a></li>			</ul>
			</aside>		<div class="squarebanner clearfix">
	<!--h3 class="sidetitl"> Sponsors </h3-->
<ul>
<!-- label::sponsors -->
</ul>
</div>	</div><!-- #secondary -->
</div>
</div></div>



	</div><!-- #content -->
	
	<div id="footer-widgets" class="clearfix">
		<div class="container"> <div class="row"> 
			<aside id="text-2" class="widget widget_text col-md-4"><h1 class="widget-title">欢迎来到 ${title}</h1>			<div class="textwidget">${desc}</div>
		</aside><aside id="categories-2" class="widget widget_categories col-md-4"><h1 class="widget-title">分类</h1>		<ul>
	
	 <#list categories as map> 
	    <li class="cat-item cat-item-2"><a href="/category/${map['alias']}/" >${map['cat']}</a>
	 </#list> 
		</ul>
</aside>		<aside id="recent-posts-2" class="widget widget_recent_entries col-md-4">		<h1 class="widget-title">最近发表</h1>		<ul>
					
					
					 <#list category\.articles as map> 
					 
					  <#if (map_index<5)>
					   <li>
				        <a href="/article/${map['_id']}.html">${map['articleTitle']}</a>
						</li>
					  </#if>	
				     </#list> 
				</ul>
		</aside>				</div></div>
	</div>
	
	<footer id="colophon" class="site-footer" role="contentinfo">
		<div class="container"><div class="row"> 
			<div class="col-md-12">
				<div class="site-info">
				<a href="http://www.miitbeian.gov.cn/" title="蜀ICP备16033307号">蜀ICP备16033307号</a> <br>
				<!-- label::footer -->
				</div><!-- .site-info -->
			</div>
		</div> </div>
	</footer><!-- #colophon -->

</div><!-- #page -->

<script type="text/javascript">var elLogo = document.getElementById("ft_logo"); if (elLogo) {elLogo.style.maxHeight = elLogo.getAttribute("relHeight") ? elLogo.getAttribute("relHeight") + "px" : "100px";} if (elLogo) {elLogo.style.maxWidth = elLogo.getAttribute("relWidth") ? elLogo.getAttribute("relWidth") + "px" : "100px";}</script><script type="text/javascript" src="/js/home_0.js"></script>
</body>
</html>