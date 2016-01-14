<!-- -------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency,
#    accountability and the service delivery of the government  organizations.
# 
#     Copyright (C) <2015>  eGovernments Foundation
# 
#     The updated version of eGov suite of products as by eGovernments Foundation
#     is available at http://www.egovernments.org
# 
#     This program is free software: you can redistribute it and/or modify
#     it under the terms of the GNU General Public License as published by
#     the Free Software Foundation, either version 3 of the License, or
#     any later version.
# 
#     This program is distributed in the hope that it will be useful,
#     but WITHOUT ANY WARRANTY; without even the implied warranty of
#     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#     GNU General Public License for more details.
# 
#     You should have received a copy of the GNU General Public License
#     along with this program. If not, see http://www.gnu.org/licenses/ or
#     http://www.gnu.org/licenses/gpl.html .
# 
#     In addition to the terms of the GPL license to be adhered to in using this
#     program, the following additional terms are to be complied with:
# 
# 	1) All versions of this program, verbatim or modified must carry this
# 	   Legal Notice.
# 
# 	2) Any misrepresentation of the origin of the material is prohibited. It
# 	   is required that all modified versions of this material be marked in
# 	   reasonable ways as different from the original version.
# 
# 	3) This license does not grant any rights to any user of the program
# 	   with regards to rights under trademark law for use of the trade names
# 	   or trademarks of eGovernments Foundation.
# 
#   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#------------------------------------------------------------------------------- -->

	<div class="panel panel-primary" data-collapsed="0" style="text-align:left">
				<div class="panel-heading">
					<div class="panel-title">
					    <s:text name="scheduleCategory.sor.category"/>
					</div>
				</div>
				<div class="panel-body">
					<div class="form-group">
						<label class="col-sm-2 control-label text-right">
						    Category Code
						</label>
						<div class="col-sm-3 add-margin">
							<s:textfield
								cssClass="form-control" name="code" maxlength="15" id="code" size="40"/>
						</div>
						<label class="col-sm-2 control-label text-right">
						    Category Name
						</label>
						<div class="col-sm-3 add-margin">
							<s:textfield
								cssClass="form-control" name="description" maxlength="150" id="description"  size="40" />
						</div>
					</div>
				</div>
	</div>
	
	<div class="row">
		<div class="col-sm-12 text-center buttonholdersearch">
			<s:submit cssClass="btn btn-primary" value="Save" id="saveButton" name="button" method="save"/> &nbsp;
			<input type="button" class="btn btn-default" value="Close" id="closeButton" name="button"
				onclick="window.close();" />
		</div>
	</div>
	
	<div class="row report-section">
				
				<br/>
				
				<div class="col-md-6 col-md-offset-3 report-table-container">
					<table align="center" width="300" border="0" cellpadding="0"
									cellspacing="0" class="table table-hover">
									<thead>
						<tr>
						  <th><s:text name="schedCategory.code"/></th>
						  <th><s:text name="schedCategory.description"/></th>
						</tr>
						</thead>
						<tbody>
						<s:iterator var="p" value="scheduleCategoryList">
							<tr>
								<td class="whitebox3wka"><s:property value="%{code}" /></td>
								<td class="whitebox3wka"><s:property value="%{description}" />
								</td>
							</tr>
						</s:iterator>
						</tbody>
					</table>
			  </div>
   </div>