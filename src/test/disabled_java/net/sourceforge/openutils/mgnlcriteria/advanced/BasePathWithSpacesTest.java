/**
 *
 * Criteria API for Magnolia CMS (http://www.openmindlab.com/lab/products/mgnlcriteria.html)
 * Copyright(C) 2009-2013, Openmind S.r.l. http://www.openmindonline.it
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.sourceforge.openutils.mgnlcriteria.advanced;

import info.magnolia.cms.core.MgnlNodeType;
import info.magnolia.context.MgnlContext;
import info.magnolia.repository.RepositoryConstants;
import it.openutils.mgnlutils.test.RepositoryTestConfiguration;
import it.openutils.mgnlutils.test.TestNgRepositoryTestcase;
import net.sourceforge.openutils.mgnlcriteria.jcr.query.AdvancedResult;
import net.sourceforge.openutils.mgnlcriteria.jcr.query.Criteria;
import net.sourceforge.openutils.mgnlcriteria.jcr.query.JCRCriteriaFactory;
import net.sourceforge.openutils.mgnlcriteria.jcr.query.ResultIterator;
import net.sourceforge.openutils.mgnlcriteria.jcr.query.criterion.Criterion;
import net.sourceforge.openutils.mgnlcriteria.jcr.query.criterion.Restrictions;

import javax.jcr.Node;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


/**
 * @author dschivo
 * @version $Id$
 */
@RepositoryTestConfiguration(jackrabbitRepositoryConfig = "/crit-repository/jackrabbit-test-configuration.xml", repositoryConfig = "/crit-repository/test-repositories.xml", bootstrapFiles = {
    "/crit-bootstrap/website.Lorem ipsum dolor sit amet.xml",
    "/crit-bootstrap/userroles.anonymous.xml",
    "/crit-bootstrap/users.system.anonymous.xml",
    "/crit-bootstrap/config.server.auditLogging.xml",
    "/crit-bootstrap/config.server.i18n.content.xml" }, security = true)
public class BasePathWithSpacesTest extends TestNgRepositoryTestcase {


    @Override
    @BeforeClass
    public void setUp() throws Exception {
        super.setUp();

        // Nodes in this workspace:
        // - Lorem ipsum dolor sit amet
        // --- consectetur adipisici elit
        MgnlContext.getJCRSession(RepositoryConstants.WEBSITE).save();

    }

    @Test(enabled = false)
    public void test() throws Exception {
        Criteria criteria = JCRCriteriaFactory.createCriteria().setWorkspace(RepositoryConstants.WEBSITE);
        criteria.setBasePath("/Lorem ipsum dolor sit amet");
        criteria.add(Restrictions.eq(Criterion.JCR_PRIMARYTYPE, MgnlNodeType.NT_PAGE));
        AdvancedResult advResult = criteria.execute();
        ResultIterator< ? extends Node> items = advResult.getItems();
        Assert.assertTrue(items.hasNext());
        Assert.assertEquals(items.next().getName(), "consectetur adipisici elit");
    }


    @Test(enabled = false)
    public void testWithParams() throws Exception {
        Criteria criteria = JCRCriteriaFactory.createCriteria().setWorkspace(RepositoryConstants.WEBSITE);
        criteria.setBasePath("//*[prop1='A' and prop2='B']/Lorem ipsum dolor sit amet");
        criteria.add(Restrictions.eq(Criterion.JCR_PRIMARYTYPE, MgnlNodeType.NT_PAGE));
        // criteria.addOrder(Order.desc("@jcr:score"));
        AdvancedResult advResult = criteria.execute();

        // if not escaped properly it will crash
        Assert.assertEquals(advResult.getItems().getSize(), 0);
        Assert.assertEquals(advResult.getTotalSize(), 0);

    }

}
