<%@page import="ivanizki.research.model.Model"
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
				<form:separator/>
				<form:inputCell name="<%=Model.ABSTRACT %>"/>
			</form:columns>
			<form:columns count="2">
				<form:separator/>
				<form:inputCell name="<%=Model.JOURNAL %>"/>
				<form:inputCell name="<%=Model.DOI %>"/>
				<form:inputCell name="<%=Model.NUMBER %>"/>
				<form:inputCell name="<%=Model.VOLUME %>"/>
				<form:inputCell name="<%=Model.PAGES %>"/>
			</form:columns>
		</form:form>
	</layout:body>
</layout:html>