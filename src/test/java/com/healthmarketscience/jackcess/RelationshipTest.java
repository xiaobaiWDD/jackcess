/*
Copyright (c) 2008 Health Market Science, Inc.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
USA

You can contact Health Market Science at info@healthmarketscience.com
or at the following address:

Health Market Science
2700 Horizon Drive
Suite 200
King of Prussia, PA 19406
*/

package com.healthmarketscience.jackcess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.healthmarketscience.jackcess.DatabaseTest.*;
import static com.healthmarketscience.jackcess.impl.JetFormatTest.*;
import com.healthmarketscience.jackcess.impl.RelationshipImpl;
import junit.framework.TestCase;

/**
 * @author James Ahlborn
 */
public class RelationshipTest extends TestCase {

  private static final Comparator<Relationship> REL_COMP = new Comparator<Relationship>() {
    public int compare(Relationship r1, Relationship r2) {
      return String.CASE_INSENSITIVE_ORDER.compare(r1.getName(), r2.getName());
    }
  };
 
  public RelationshipTest(String name) throws Exception {
    super(name);
  }

  public void testTwoTables() throws Exception {
    for (final TestDB testDB : TestDB.getSupportedForBasename(Basename.INDEX, true)) {
      Database db = open(testDB);
      Table t1 = db.getTable("Table1");
      Table t2 = db.getTable("Table2");
      Table t3 = db.getTable("Table3");

      List<Relationship> rels = db.getRelationships(t1, t2);
      assertEquals(1, rels.size());
      Relationship rel = rels.get(0);
      assertEquals("Table2Table1", rel.getName());
      assertEquals(t2, rel.getFromTable());
      assertEquals(Arrays.asList(t2.getColumn("id")),
                   rel.getFromColumns());
      assertEquals(t1, rel.getToTable());
      assertEquals(Arrays.asList(t1.getColumn("otherfk1")),
                   rel.getToColumns());
      assertTrue(rel.hasReferentialIntegrity());
      assertEquals(4096, ((RelationshipImpl)rel).getFlags());
      assertTrue(rel.cascadeDeletes());
      assertSameRelationships(rels, db.getRelationships(t2, t1), true);

      rels = db.getRelationships(t2, t3);
      assertTrue(db.getRelationships(t2, t3).isEmpty());
      assertSameRelationships(rels, db.getRelationships(t3, t2), true);

      rels = db.getRelationships(t1, t3);
      assertEquals(1, rels.size());
      rel = rels.get(0);
      assertEquals("Table3Table1", rel.getName());
      assertEquals(t3, rel.getFromTable());
      assertEquals(Arrays.asList(t3.getColumn("id")),
                   rel.getFromColumns());
      assertEquals(t1, rel.getToTable());
      assertEquals(Arrays.asList(t1.getColumn("otherfk2")),
                   rel.getToColumns());
      assertTrue(rel.hasReferentialIntegrity());
      assertEquals(256, ((RelationshipImpl)rel).getFlags());
      assertTrue(rel.cascadeUpdates());
      assertSameRelationships(rels, db.getRelationships(t3, t1), true);

      try {
        db.getRelationships(t1, t1);
        fail("IllegalArgumentException should have been thrown");
      } catch(IllegalArgumentException ignored) {
        // success
      }
    }
  }

  public void testOneTable() throws Exception {
    for (final TestDB testDB : TestDB.getSupportedForBasename(Basename.INDEX, true)) {
      Database db = open(testDB);
      Table t1 = db.getTable("Table1");
      Table t2 = db.getTable("Table2");
      Table t3 = db.getTable("Table3");

      List<Relationship> expected = new ArrayList<Relationship>();
      expected.addAll(db.getRelationships(t1, t2));
      expected.addAll(db.getRelationships(t2, t3));

      assertSameRelationships(expected, db.getRelationships(t2), false);
      
    }
  }

  public void testNoTables() throws Exception {
    for (final TestDB testDB : TestDB.getSupportedForBasename(Basename.INDEX, true)) {
      Database db = open(testDB);
      Table t1 = db.getTable("Table1");
      Table t2 = db.getTable("Table2");
      Table t3 = db.getTable("Table3");

      List<Relationship> expected = new ArrayList<Relationship>();
      expected.addAll(db.getRelationships(t1, t2));
      expected.addAll(db.getRelationships(t2, t3));
      expected.addAll(db.getRelationships(t1, t3));

      assertSameRelationships(expected, db.getRelationships(), false);
    }
  }

  private static void assertSameRelationships(
      List<Relationship> expected, List<Relationship> found, boolean ordered)
  {
    assertEquals(expected.size(), found.size());
    if(!ordered) {
      Collections.sort(expected, REL_COMP);
      Collections.sort(found, REL_COMP);
    }
    for(int i = 0; i < expected.size(); ++i) {
      Relationship eRel = expected.get(i);
      Relationship fRel = found.get(i);
      assertEquals(eRel.getName(), fRel.getName());
    }
  }
  
}