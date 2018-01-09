
<!DOCTYPE html>
<html lang="en-US">
<head>
	
	<link rel="stylesheet" type="text/css" href="./css/css_0.css" media="all" />
  
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="keywords" content="${keyword}">
<meta name="description" content="${desc}">
<title>${title} 	| ${title1}</title>

<script type="text/javascript" src="./js/one.js"></script>
</head>

<body class="home page page-id-2 page-template page-template-homepage page-template-homepage-php group-blog">
<div id="page" class="hfeed site">

<div id="scotch-panel">
	<div class="menu-menu-1-container"><ul id="menu-menu-1" class="menu"><li id="menu-item-85" class="menu-item menu-item-type-post_type menu-item-object-page current-menu-item page_item page-item-2 current_page_item menu-item-85"><a href="index.html">首页</a></li>
     <#list categories as map>
        <li id="menu-item-88" class="menu-item menu-item-type-taxonomy menu-item-object-category menu-item-has-children menu-item-88"><a href="./category/${map['alias']}/">${map['cat']}</a></li>
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
					<div class="menu-menu-1-container"><ul id="isomer" class="menu"><li class="menu-item menu-item-type-post_type menu-item-object-page current-menu-item page_item page-item-2 current_page_item menu-item-85"><a href="index.html">首页</a></li>

<li class="menu-item menu-item-type-taxonomy menu-item-object-category menu-item-has-children menu-item-88"><a href="./category/">分类</a>
<ul class="sub-menu">

     <#list categories as map> 
        <li class="menu-item menu-item-type-taxonomy menu-item-object-category menu-item-89"><a href="./category/${map['alias']}/">${map['cat']}</a></li>
	 </#list> 
</ul>
</li>
<li class="menu-item menu-item-type-taxonomy menu-item-object-category menu-item-has-children menu-item-88"><a href="./tag/">标签</a>
<ul class="sub-menu">
     <#list tags as map> 
        <li class="menu-item menu-item-type-taxonomy menu-item-object-category menu-item-89"><a href="./tag/${map['alias']}/">${map['cat']}</a></li>
	 </#list> 
	
</ul>
</li>
<li class="menu-item menu-item-type-post_type menu-item-object-page menu-item-82"><a href="./about/">关于..</a></li>
</ul></div>				</nav><!-- #site-navigation -->
			</div>
		</div></div>
	</header><!-- #masthead -->

	<div id="content" class="site-content">
<div class="welcome-box">
	<div class="container"><div class="row">
		<div class="col-md-12">
			<h2 class="welcome-message">${welcome}</h2>
		</div>
	</div></div>
</div>

<div class="portfolio-home">
	<div class="container"><div class="row">

		<div class="col-md-12">
			<!--ul id="folio-filter">
			<li class="active"><a href="javascript:void(0)" title="" data-filter=".all">全部</a></li>
			
			
			 <#list categories as map> 
			 <li><a href="javascript:void(0)" title="" data-filter=".${map['alias']}">${map['cat']}</a></li>
			 </#list>      
			</ul>-->
		</div>

		<div class="clear"></div>

		<div class="portfolio-box">
		
		                <#list articles as map> 
						<div class="folio-item col-md-3 col-xs-6 ${map['_id']}">
					         <div class="portfolio-thumb">
												
							    <div class="folio-pic">
								    <div class="folio-overlay">
									<h3> <a href="/article/${map['_id']}.html"> ${map['articleTitle']} </a></h3>
									
									<#list map['article.categories'] as map2>
									分类: <a href="/category/${map2['alias']}/" rel="tag">${map2['cat']} </a>							
				                    </#list> 
									
									</div>
								    <a href="/article/${map['_id']}.html"> <img src="${map['imgUrlSmall']}" alt="${map['articleTitle']}" /> </a>
							    </div>
					        </div>
				        </div>
				        </#list> 
	    				
	    			</div>
	</div></div>
</div>


<div class="blog-home">
	<div class="container"> <div class="row"> 
			
			
			 <#list articles as map> 
			 <div class="col-md-12 home-post">
					<div class="row">
						<div class="col-md-6 home-post-title">
							<h2> <a href="./article/${map['_id']}.html"> ${map['articleTitle']} </a></h2>
							<div class="home-post-meta">
								<div> 
									<i class="fa fa-user"></i>  ${map['author']}
								</div>
								<div>
									<i class="fa fa-clock-o"></i> ${map['dateTime']}								</div>
								<div>
								
								<#list map['article.categories'] as map2>
									<i class="fa fa-tag"></i> <a href="./category/${map2['alias']}/" rel="category tag">${map2['cat']} </a>
																
				                    </#list> 
				                    
																	
									
									</div>
								<!--div>
									 <i class="fa fa-comment"></i> 1 Comment								</div-->
							</div>
						</div>
						<div class="col-md-6 home-post-content">

							<a href="./article/${map['_id']}.html"> <img class="postimg" src="${map['imgUrlLarge']}" alt="${map['articleTitle']}" /> </a>
														
							<p>${map['content']} [&hellip;]</p>
							
							<a href="./article/${map['_id']}.html" class="readmore"> 全 文 </a>

						</div>
					</div>
				</div>
			 </#list>
				

			

				</div></div>	
</div>



	</div><!-- #content -->
	
	<div id="footer-widgets" class="clearfix">
		<div class="container"> <div class="row"> 
			<aside id="text-2" class="widget widget_text col-md-4"><h1 class="widget-title">欢迎来到 ${title}</h1>			<div class="textwidget">${desc}</div>
		</aside><aside id="categories-2" class="widget widget_categories col-md-4"><h1 class="widget-title">分类</h1>		<ul>
	
	 <#list categories as map> 
	    <li class="cat-item cat-item-2"><a href="./category/${map['alias']}/" >${map['cat']}</a>
	 </#list> 
		</ul>
</aside>		<aside id="recent-posts-2" class="widget widget_recent_entries col-md-4">		<h1 class="widget-title">最近发表</h1>		<ul>
					
					
					 <#list articles as map> 
					 
					 <#if (map_index<5)>
					   <li>
				        <a id="article_index_${map_index}" href="./article/${map['_id']}.html">${map['articleTitle']}</a>
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

<script type="text/javascript">var elLogo = document.getElementById("ft_logo"); if (elLogo) {elLogo.style.maxHeight = elLogo.getAttribute("relHeight") ? elLogo.getAttribute("relHeight") + "px" : "100px";} if (elLogo) {elLogo.style.maxWidth = elLogo.getAttribute("relWidth") ? elLogo.getAttribute("relWidth") + "px" : "100px";}</script><script type="text/javascript" src="./js/home_0.js"></script>
</body>
</html>

