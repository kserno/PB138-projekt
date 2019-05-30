<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0" xmlns="http://www.w3.org/1999/xhtml" xmlns:e="http://europass.cedefop.europa.eu/Europass">
<xsl:output method="xml" doctype-public="-//W3C//DTD XHTML 1.0 Strict//EN" doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd" encoding="UTF-8" indent="yes" />

<xsl:template match="/e:SkillsPassport">
	<html lang="en">
		<head>
			<meta charset="UTF-8" />
			<meta name="viewport" content="width=device-width, initial-scale=1.0" />
			<meta http-equiv="X-UA-Compatible" content="ie=edge" />
			<link href='http://fonts.googleapis.com/css?family=Rokkitt:400,700|Lato:400,300' rel='stylesheet' type='text/css' />
			<link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.8.2/css/all.css" integrity="sha384-oS3vJWv+0UjzBfQzYUhtDYW+Pj2yciDJxpsK1OYPAYjqT085Qq/1cq5FLXAZQ7Ay" crossorigin="anonymous" />
			<title>Europass CV Viewer</title>
		</head>
		<xsl:apply-templates mode="style" select="/" />
		<xsl:apply-templates mode="body" select="/e:SkillsPassport" />
	</html>
</xsl:template>
	
<!-- ______________________ HTML BODY ______________________ -->
<xsl:template mode="body" match="//e:LearnerInfo">
	<body>
		<div id="cv">
			<div class="mainDetails">
				<xsl:apply-templates mode="header" select="." />
			</div>
			<div id="mainArea">
				<xsl:apply-templates mode="workExperienceSection" 
				select="./e:WorkExperienceList" />

				<xsl:apply-templates mode="educationSection" 
				select="./e:EducationList" />

				<xsl:apply-templates mode="languagesSection" 
				select="./e:Skills/e:Linguistic" />

				<xsl:apply-templates mode="otherSkills" 
				select="./e:Skills" />

				<xsl:apply-templates mode="achievementsSection" 
				select="./e:AchievementList" />
			</div>
		</div>
	</body>
</xsl:template>

<!-- ______________________ ACHIEVEMNT ARTICLE ______________________ -->
<xsl:template mode="achievement" match="e:Achievement">
	<article>
		<h2><xsl:value-of select="e:Title/e:Label" /></h2>
		<p><xsl:value-of select="e:Description" /></p>
	</article>
</xsl:template>

<!-- ______________________ ACHIEVEMENTS SECTION ______________________ -->
<xsl:template mode="achievementsSection" match="e:AchievementList">
	<section>
		<div class="sectionTitle">
			<h1>Achievements</h1>
		</div>
		<div class="sectionContent">
			<xsl:apply-templates mode="achievement" select="e:Achievement" />
		</div>
		<div class="clear"></div>
	</section>
</xsl:template>

<!-- ______________________ UNIVERSITY NAME ______________________ -->
<xsl:template mode="university" match="e:Organisation">
	<xsl:value-of select="e:Name" />, 
	<xsl:value-of select="e:ContactInfo/e:Address/e:Contact/e:Municipality" />
	[<xsl:value-of select="e:ContactInfo/e:Address/e:Contact/e:Country/e:Code" />]
</xsl:template>

<!-- ______________________ EDUCATION ARTICLE ______________________ -->
<xsl:template mode="education" match="e:Education">
	<article>
		<h2>
			<xsl:apply-templates mode="university" 
			select="e:Organisation" />
		</h2>
		<p class="subDetails"><xsl:apply-templates mode="period" select="e:Period" /></p>
		<p><xsl:value-of select="e:Title" /></p>
	</article>
</xsl:template>

<!-- ______________________ EDUCATION SECTION ______________________ -->
<xsl:template mode="educationSection" match="e:EducationList">
	<section>
		<div class="sectionTitle">
			<h1>Education</h1>
		</div>
		<div class="sectionContent">
			<xsl:apply-templates mode="education" select="e:Education" />
		</div>
		<div class="clear"></div>
	</section>
</xsl:template>

<!-- ______________________ SPECIFIC SKILL SECTION ______________________ -->
<xsl:template name="otherSkill">
	<xsl:param name="skillName" />
	<xsl:param name="description" />
	<article>
		<h2><xsl:value-of select="$skillName" /></h2>
		<p><xsl:value-of select="$description" disable-output-escaping="yes"/></p>
	</article>
</xsl:template>

<!-- ______________________ OTHER SKILLS SECTION ______________________ -->
<xsl:template mode="otherSkills" match="e:Skills">
	<section>
		<div class="sectionTitle">
			<h1>Other Skills</h1>
		</div>
		<div class="sectionContent">
			<xsl:call-template name="otherSkill">
				<xsl:with-param name="skillName" select='"Communication Skills"' />
				<xsl:with-param name="description" select="e:Communication/e:Description" />
			</xsl:call-template>

			<xsl:call-template name="otherSkill">
				<xsl:with-param name="skillName" select='"Organisational Skills"' />
				<xsl:with-param name="description" select="e:Organisational/e:Description" />
			</xsl:call-template>

			<xsl:call-template name="otherSkill">
				<xsl:with-param name="skillName" select='"Computer Skills"' />
				<xsl:with-param name="description" select="e:Computer/e:Description" />
			</xsl:call-template>

			<xsl:call-template name="otherSkill">
				<xsl:with-param name="skillName" select='"Other"' />
				<xsl:with-param name="description" select="e:Other/e:Description" />
			</xsl:call-template>
		</div>
		<div class="clear"></div>
	</section>
</xsl:template>


<!-- ______________________ FOREIGN LANGUAGES SECTION ______________________ -->
<xsl:template mode="foreignLanguages" match="e:ForeignLanguageList/e:ForeignLanguage">
	<li>
		<span class="language-name"><xsl:value-of select="./e:Description/e:Label" /></span>
		<ul>
			<li><i class="fas fa-headphones-alt" />Listening: 
			<xsl:value-of select="./e:ProficiencyLevel/e:Listening" /></li>
			
			<li><i class="fas fa-book" />Reading: 
			<xsl:value-of select="./e:ProficiencyLevel/e:Reading" /></li>
			
			<li><i class="fas fa-user-friends" />Verbal: 
			<xsl:value-of select="./e:ProficiencyLevel/e:SpokenInteraction" /></li>
		</ul>
	</li>
</xsl:template>

<!-- ______________________ LANGUAGES SECTION ______________________ -->
<xsl:template mode="languagesSection" match="e:Linguistic">
	<section>
		<div class="sectionTitle">
			<h1>Languages</h1>
		</div>

		<div class="sectionContent">
			<ul class="keySkills">
				<xsl:apply-templates mode="foreignLanguages" 
				select="e:ForeignLanguageList/e:ForeignLanguage" />
			</ul>
		</div>
		<div class="clear"></div>
	</section>
</xsl:template>

<!-- ______________________ WORK EXPERIENCE SECTION ______________________ -->
<xsl:template mode="workExperienceSection" match="e:WorkExperienceList">
	<section>
		<div class="sectionTitle">
			<h1>Work Experience</h1>
		</div>
		<div class="sectionContent">
			<xsl:apply-templates mode="workExperience" select="e:WorkExperience" />
		</div>
		<div class="clear"></div>
	</section>
</xsl:template>

<!-- ______________________ WORK EXPERIENCE ARTICLE ______________________ -->
<xsl:template mode="workExperience" match="e:WorkExperience" >
	<article>
		<h2><xsl:value-of select="e:Position/e:Label" /></h2>
		<p class="subDetails">
			<xsl:apply-templates mode="period" select="e:Period" />
		</p>
		<p class="subDetails">
			<xsl:apply-templates mode="employer" select="e:Employer" />
		</p>
		<p><xsl:value-of select="e:Activities" disable-output-escaping="yes"/></p>
	</article>
</xsl:template>

<!-- ______________________ PERIOD PARAGRAPH ______________________ -->
<xsl:template mode="period" match="e:Period">
	<xsl:value-of select="e:From/@year" />
	<xsl:apply-templates mode="month" select=".[e:From/@month]">
		<xsl:with-param name="month-num" select="e:From/@month" />
	</xsl:apply-templates>
	 - 
	<xsl:choose>
		<xsl:when test="e:Current[text()='true']">Present</xsl:when>
		<xsl:otherwise>
			<xsl:value-of select="e:To/@year" />
			<xsl:apply-templates mode="month" select=".[e:To/@month]">
				<xsl:with-param name="month-num" select="e:To/@month" />
			</xsl:apply-templates>
		</xsl:otherwise>
	</xsl:choose>
</xsl:template>

<!-- ______________________ MONTH NAME SWITCH ______________________ -->
<xsl:template mode="month" match="e:Period">
    <xsl:param name="month-num" />
	<xsl:choose>
		<xsl:when test="data($month-num)='--01'">/Jan</xsl:when>
		<xsl:when test="data($month-num)='--02'">/Feb</xsl:when>
		<xsl:when test="data($month-num)='--03'">/Mar</xsl:when>
		<xsl:when test="data($month-num)='--04'">/Apr</xsl:when>
		<xsl:when test="data($month-num)='--05'">/May</xsl:when>
		<xsl:when test="data($month-num)='--06'">/June</xsl:when>
		<xsl:when test="data($month-num)='--07'">/July</xsl:when>
		<xsl:when test="data($month-num)='--08'">/Aug</xsl:when>
		<xsl:when test="data($month-num)='--09'">/Sept</xsl:when>
		<xsl:when test="data($month-num)='--10'">/Oct</xsl:when>
		<xsl:when test="data($month-num)='--11'">/Nov</xsl:when>
		<xsl:when test="data($month-num)='--12'">/Dec</xsl:when>
	</xsl:choose>	
</xsl:template>

<!-- ______________________ EMPLOYER PARAGRAPH ______________________ -->
<xsl:template mode="employer" match="e:Employer">
	<xsl:value-of select="e:Name" /> [
	<xsl:value-of select="e:ContactInfo/e:Address/e:Contact/e:Municipality" />
	<xsl:text>, </xsl:text>
	<xsl:value-of select="e:ContactInfo/e:Address/e:Contact/e:Country/e:Code" />]
</xsl:template>

<!-- ______________________ PERSON INFO TOP SECTION ______________________ -->
<xsl:template mode="header" match="//e:LearnerInfo">
	<div id="name">
		<h1>
			<xsl:value-of select="//e:FirstName" />
			<xsl:text> </xsl:text>
			<xsl:value-of select="//e:Surname" />
		</h1>
		<h2>
			<xsl:value-of select="e:Headline/e:Description/e:Label" />
		</h2>
	</div>

	<div id="contactDetails">
		<ul>
			<xsl:apply-templates mode="contact" 
			select="//e:LearnerInfo/e:Identification/e:ContactInfo" />
		</ul>
	</div>
	<div class="clear"></div>
</xsl:template>


<!-- ______________________ CONTACT LEFT TOP SECTION ______________________ -->
<xsl:template mode="contact" match="//e:LearnerInfo/e:Identification/e:ContactInfo">
	<li>
		<i class="fas fa-at"></i>
		<xsl:value-of select="e:Email/e:Contact" />
	</li>
	<li><i class="fas fa-phone"></i>
		<xsl:apply-templates mode="phone" select="e:TelephoneList/e:Telephone"/>
	</li>
	<li><i class="fas fa-address-card"></i>
		<xsl:value-of select="e:Address/e:Contact/e:Municipality" />
		<xsl:text>, </xsl:text>
		<xsl:value-of select="e:Address/e:Contact/e:Country/e:Code" />
	</li>
</xsl:template>

<!-- ______________________ PHONE SELECTOR ______________________ -->
<xsl:template mode="phone" match="e:TelephoneList/e:Telephone">
	<xsl:if test="./e:Use/e:Code[text()='mobile']">
		<xsl:value-of select="./e:Contact"></xsl:value-of>
	</xsl:if>
</xsl:template>


<!-- ______________________ CSS SECTION ______________________ -->
<xsl:template match="/" mode="style">
	<style>
		* {
			border:0;
			font:inherit;
			font-size:100%;
			margin:0;
			padding:0;
			vertical-align:baseline;
		}

		article,aside,details,figcaption,figure,footer,header,hgroup,menu,nav,section {
			display:block;
		}

		html, body {
			background: #181818; 
			font-family: 'Lato', 
			helvetica, arial, 
			sans-serif; 
			font-size: 16px; 
			color: #222;
		}

		.clear {
			clear: both;
		}

		p {
			font-size: 1em;
			line-height: 1.4em;
			margin-bottom: 20px;
			color: #444;
		}

		#cv {
			width: 90%;
			max-width: 800px;
			background: #f3f3f3;
			margin: 30px auto;
		}

		.mainDetails {
			padding: 25px 35px;
			border-bottom: 2px solid #cf8a05;
			background: #ededed;
		}

		#name h1 {
			font-size: 2.5em;
			font-weight: 700;
			font-family: 'Rokkitt', Helvetica, Arial, sans-serif;
			margin-bottom: -6px;
		}

		#name h2 {
			font-size: 2em;
			margin-left: 2px;
			font-family: 'Rokkitt', Helvetica, Arial, sans-serif;
		}

		#mainArea {
			padding: 0 40px;
		}

		#headshot {
			width: 12.5%;
			float: left;
			margin-right: 30px;
		}

		#headshot img {
			width: 100%;
			height: auto;
			-webkit-border-radius: 50px;
			border-radius: 50px;
		}

		#name {
			float: left;
		}

		#contactDetails {
			float: right;
		}

		#contactDetails ul {
			list-style-type: none;
			font-size: 0.9em;
			margin-top: 2px;
		}

		#contactDetails ul li {
			margin-bottom: 3px;
			color: #444;
		}

		#contactDetails ul li a, a[href^=tel] {
			color: #444; 
			text-decoration: none;
			-webkit-transition: all .3s ease-in;
			-moz-transition: all .3s ease-in;
			-o-transition: all .3s ease-in;
			-ms-transition: all .3s ease-in;
			transition: all .3s ease-in;
		}

		#contactDetails ul li a:hover { 
			color: #cf8a05;
		}


		section {
			border-top: 1px solid #dedede;
			padding: 20px 0 0;
		}

		section:first-child {
			border-top: 0;
		}

		section:last-child {
			padding: 20px 0 10px;
		}

		.sectionTitle {
			float: left;
			width: 25%;
		}

		.sectionContent {
			float: right;
			width: 72.5%;
		}

		.sectionTitle h1 {
			font-family: 'Rokkitt', Helvetica, Arial, sans-serif;
			font-style: italic;
			font-size: 1.5em;
			color: #cf8a05;
		}

		.sectionContent h2 {
			font-family: 'Rokkitt', Helvetica, Arial, sans-serif;
			font-size: 1.5em;
			margin-bottom: -2px;
		}

		.subDetails {
			font-size: 0.8em;
			font-style: italic;
			margin-bottom: 3px;
		}

		.keySkills {
			list-style-type: none;
			-moz-column-count: <xsl:value-of select="count(//e:ForeignLanguage)"/>;
			-webkit-column-count: <xsl:value-of select="count(//e:ForeignLanguage)"/>;
			column-count: <xsl:value-of select="count(//e:ForeignLanguage)"/>;
			margin-bottom: 20px;
			font-size: 1em;
			color: #444;
		}

		.language-name {
			font-weight: bold;
		}

		.keySkills ul li {
			margin-bottom: 3px;
		}

		.fas {
			margin-right: 5px;
		}

		@media all and (min-width: 602px) and (max-width: 800px) {
			#headshot {
				display: none;
			}

			.keySkills {
			-moz-column-count:2;
			-webkit-column-count:2;
			column-count:2;
			}
		}

		@media all and (max-width: 601px) {
			#cv {
				width: 95%;
				margin: 10px auto;
				min-width: 280px;
			}

			#headshot {
				display: none;
			}

			#name, #contactDetails {
				float: none;
				width: 100%;
				text-align: center;
			}

			.sectionTitle, .sectionContent {
				float: none;
				width: 100%;
			}

			.sectionTitle {
				margin-left: -2px;
				font-size: 1.25em;
			}

			.keySkills {
				-moz-column-count:2;
				-webkit-column-count:2;
				column-count:2;
			}
		}

		@media all and (max-width: 480px) {
			.mainDetails {
				padding: 15px 15px;
			}

			section {
				padding: 15px 0 0;
			}

			#mainArea {
				padding: 0 25px;
			}


			.keySkills {
			-moz-column-count:1;
			-webkit-column-count:1;
			column-count:1;
			}

			#name h1 {
				line-height: .8em;
				margin-bottom: 4px;
			}
		}

	</style>
</xsl:template>

</xsl:stylesheet>