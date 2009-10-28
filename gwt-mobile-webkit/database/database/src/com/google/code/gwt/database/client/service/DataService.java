/*
 * Copyright 2009 Bart Guijt and others.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.code.gwt.database.client.service;

import java.util.Date;
import java.util.List;

import com.google.code.gwt.database.client.Database;
import com.google.code.gwt.database.client.SQLTransaction;
import com.google.code.gwt.database.client.service.annotation.Connection;
import com.google.code.gwt.database.client.service.annotation.SQL;

/**
 * Marks the interface as a DataService.
 * 
 * <p>
 * The DataService interface is the starting point for defining a SQL
 * DataService. Start by defining your DataService like so (substitute anything
 * in <i>italics</i> with your own values):
 * </p>
 * 
 * <pre>
 * &#x40;Connection(name="<i>shortname</i>", version="<i>1.0</i>",
 *             description="<i>diplayname</i>", maxsize=<i>max size in bytes</i>)
 * public interface <i>MyDataService</i> extends DataService {
 *   // ...
 * }
 * </pre>
 * 
 * <p>
 * Please make sure that you both extend the DataService interface, and add a
 * Connection annotation to it to specify the database
 * {@link Database#openDatabase(String, String, String, int) connection
 * parameters}.
 * </p>
 * 
 * <p>
 * Next, specify the actual data services as interface methods. There are three
 * kinds of DataService methods, like in the following code:
 * </p>
 * 
 * <pre>
 * // 1. No resultset wanted (or expected):
 * &#x40;{@link SQL}(<i>"INSERT INTO a_table (column_name) VALUES ({when.getTime()})"</i>)
 * void <i>insertValue</i>(<i>Date when</i>, {@link VoidCallback} callback);
 * 
 * // 2. A scalar value wanted (or expected):
 * &#x40;{@link SQL}(<i>"SELECT COUNT(*) FROM a_table"</i>)
 * void <i>getValueCount</i>({@link ScalarCallback}&lt;<i>Integer</i>&gt; callback);
 * 
 * // 3. A List of (row) items wanted (or expected):
 * &#x40;{@link SQL}(<i>"SELECT * FROM a_table WHERE column_name &gt;= {when.getTime()}"</i>)
 * void <i>getValuesFrom</i>(<i>Date when</i>, {@link ListCallback}&lt;<i>GenericRow</i>&gt; callback);
 * </pre>
 * 
 * <p>
 * Before we take the different callbacks one-by-one, first this:
 * </p>
 * <ul>
 * <li>Each defined method in the interface must be annotated with the
 * {@link SQL} annotation. This specifies the SQL statements(that's right, one
 * or more) to execute whenever the method is called. Potential results are
 * <em>only</em> collected from the last SQL statement.</li>
 * <li>All specified SQL statements are executed within a single
 * {@link SQLTransaction SQL transaction}.</li>
 * <li>Use curly brackets to map the method's parameters to a value in the SQL
 * statement.</li>
 * <li>You can re-use a parameter as much as you want (in the same statement or
 * over different statements).</li>
 * <li>You can enter Java expressions within the curly braces, as long as they
 * evaluate to a single Object.</li>
 * </ul>
 * 
 * <p>
 * These are the callback types you can use:
 * </p>
 * <ol>
 * <li>The {@link VoidCallback 'void' callback}. This callback doesn't do
 * anything with the query resultset, if any.</li>
 * <li>The {@link ScalarCallback 'scalar' callback}. This callback expects the
 * resultset to have exactly one record in the resultset, with just one column
 * of data (like a <code>COUNT()</code> query). Any other records, or columns,
 * are just ignored. There are only a few scalar types suitable for this
 * callback: {@link Integer}, {@link String}, {@link Boolean}, {@link Double},
 * {@link Float} and {@link Date}.</li>
 * <li>The {@link ListCallback 'list' callback}. This type relays the complete
 * resultset as a {@link List} to the caller. The type parameter of the callback
 * is the same as the type parameter on the returned List.</li>
 * </ol>
 * 
 * <p>
 * Now that we have a DataService, this is how you use it:
 * </p>
 * 
 * <pre>
 * MyDataService service = GWT.create(MyDataService.class);
 * 
 * service.insertValue(new Date(), new VoidCallback() {
 *   public boolean onFailure(SQLError e) {
 *     return true;  // rollback transaction
 *   }
 *   public void onSuccess() {
 *     // What to do when transaction succeeded?
 *   }
 * });
 * </pre>
 * 
 * <p>
 * Due to the asynchronous nature of the Database API, we must employ callbacks
 * instead of regular return values. Hopefully, either GWT will incorporate
 * something like <a href="http://www.neilmix.com/narrativejs/doc/">Narrative
 * JS</a> or <a href="http://chumsley.org/jwacs/">Jwacs</a>, or the <a
 * href="http://www.w3.org/TR/webdatabase/#synchronous-database-api">synchronous
 * Database API</a> makes its inroads in the near future.
 * </p>
 * 
 * @see Connection
 * @see SQL
 * @see VoidCallback
 * @see ScalarCallback
 * @see ListCallback
 * @see <a
 *      href="http://code.google.com/p/gwt-mobile-webkit/wiki/DataServiceUserGuide">Wiki:
 *      User Guide</a>
 * 
 * @author bguijt
 */
public interface DataService {

  /**
   * Returns the Database associated with this DataService.
   */
  Database getDatabase();

}
