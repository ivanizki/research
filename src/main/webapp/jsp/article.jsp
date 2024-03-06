<%@page import="ivanizki.research.model.Model"
%><%@page import="ivanizki.research.layout.manuscript.ArticleFormContextProvider"
%><%@page import="ivanizki.research.layout.manuscript.DoiLinkRenderer"
%><%@page language="java" extends="com.top_logic.util.TopLogicJspBase"
%><%@taglib uri="ajaxform" prefix="form"
%><%@taglib uri="layout" prefix="layout"
%><layout:html>
	<layout:head/>
	<layout:body>
		<form:form ignoreModel="true">
			<form:columns count="1">
				<form:inputCell name="<%=Model.TITLE %>"/>
				<form:inputCell name="<%=Model.AUTHORS %>"/>
			</form:columns>
			<form:columns count="2">
				<form:inputCell name="<%=Model.YEAR %>"/>
				<form:inputCell name="<%=Model.TOPICS %>"/>
			</form:columns>
			<form:columns count="1">
				<form:inputCell name="<%=Model.ABSTRACT %>"/>
			</form:columns>
			<form:columns count="2">
				<form:inputCell name="<%=Model.FILE_PATH %>"/>
				<form:ifExists name="<%=ArticleFormContextProvider.DOI_LINK %>">
					<form:descriptionCell>
					<form:description>
					<form:label name="<%=ArticleFormContextProvider.DOI_LINK %>"/>
					</form:description>
					<form:custom name="<%=ArticleFormContextProvider.DOI_LINK %>"/>
					</form:descriptionCell>
				</form:ifExists>
				<form:ifExists name="<%=ArticleFormContextProvider.DOI_LINK %>"
					not="true"
				>
					<form:inputCell name="<%=Model.DOI %>"/>
				</form:ifExists>
			</form:columns>
			<form:columns count="2">
				<form:separator/>
				<form:inputCell name="<%=Model.JOURNAL %>"/>
				<form:inputCell name="<%=Model.PAGES %>"/>
				<form:inputCell name="<%=Model.VOLUME %>"/>
				<form:inputCell name="<%=Model.NUMBER %>"/>
			</form:columns>
		</form:form>
	</layout:body>
</layout:html>