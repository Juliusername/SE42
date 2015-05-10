/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bank.dao;

import bank.domain.Account;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.NoResultException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import util.DatabaseCleaner;

/**
 *
 * @author Twan
 */
public class AccountDAOTest
{
    private final EntityManagerFactory emf;
    private EntityManager em;
    
    public AccountDAOTest()
    {
        this.emf = Persistence.createEntityManagerFactory("bankPU");
        this.em = emf.createEntityManager();

    }
    
    @BeforeClass
    public static void setUpClass() {}
    
    @AfterClass
    public static void tearDownClass() {}
    
    @Before
    public void setUp()
    {
        try {
            // clean database
            DatabaseCleaner dc = new DatabaseCleaner(this.em);
            dc.clean();
            
        } catch (SQLException ex) {
            Logger.getLogger(AccountDAOTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // databasecleaner closes connection
        this.em = emf.createEntityManager();
    }
    
    @After
    public void tearDown() {}

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    //1.	Wat is de waarde van asserties en printstatements? Corrigeer verkeerde asserties zodat de test ‘groen’ wordt.
    //2.	Welke SQL statements worden gegenereerd?
    //3.	Wat is het eindresultaat in de database?
    //4.	Verklaring van bovenstaande drie observaties.

    // OPDRACHT 1
    
    @Test
    public void commitTest()
    {
        Account account = new Account(111L);
        
        System.out.println("--- START COMMIT TEST");
        
        em.getTransaction().begin();
        em.persist(account);
        
        // Nieuw object account object wordt gemaakt met nog geen Id nummer.
        // Deze wordt pas aangemaakt wanneer de transactie gecommit is door middel
        // van de volgende annotatie:
        //      @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        //
        // waarde = null
        assertNull(account.getId());
        
        // Bij de commit wordt de volgende statements uitgevoerd:
        //      INSERT INTO ACCOUNT (ACCOUNTNR, BALANCE, THRESHOLD) VALUES (111, 0, 0)
        //      SELECT LAST_INSERT_ID()
        em.getTransaction().commit();
        
        // waarde = 1
        System.out.println("AccountId: " + account.getId());
        
        // Na de commit wordt het Id nummer gelijk aan returnde id nummer van de statement.
        //
        // waarde = true
        assertTrue(account.getId() > 0L); 
        
        // EINDRESULTAAT:
        // In de database staat nu een rij in de tabel account met de waardes:
        //      ID = 1, ACCOUNTNR = 111, BALANCE = 0, THRESHOLD = 0
    }
    
    // OPDRACHT 2
    
    @Test (expected=NoResultException.class)
    public void rollbackTest()
    {
        Account account = new Account(111L);
        
        System.out.println("--- START ROLLBACK TEST");
        
        em.getTransaction().begin();    
        em.persist(account);
        
        // Nieuw object account object wordt gemaakt met nog geen Id nummer.
        // Deze wordt pas aangemaakt wanneer de transactie gecommit is door middel
        // van de volgende annotatie:
        //      @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        //
        // waarde = null
        assertNull(account.getId());
        
        // Detach account object door middel van een rollback. Hierbij wordt de volgende
        // statement uitgevoerd:
        //      SELECT ID, ACCOUNTNR, BALANCE, THRESHOLD FROM ACCOUNT WHERE (ACCOUNTNR = 111)
        em.getTransaction().rollback();
        
        // Om te controlleren dat het account ook daadwerkelijk niet bestaat gebruiken
        // we de AccountDAOJPAImpl om het account te zoeken.
        AccountDAOJPAImpl accountDAO = new AccountDAOJPAImpl(em);
        
        // Voor het zoeken naar een account door middel van account nummer wordt de volgende SQL
        // statement gebruikt (zoals gedefinieerd in Account.java):
        //      select a from Account as a where a.accountNr = :accountNr 
        //
        // waarde = null (throws NoResultException in getSingleResult())
        accountDAO.findByAccountNr(111L);  
        
        // EINDRESULTAAT:
        // In de database staan geen rijen
    }
    
    // OPDRACHT 3
    
    @Test
    public void flushTest()
    {
        Long expected = -100L;
        Account account = new Account(111L);
        account.setId(expected);
        
        System.out.println("--- START FLUSH TEST");
        
        em.getTransaction().begin();
        em.persist(account);
        
        // Aangezien het id nummer handmatig is geset en niet van de statement komt,
        // heeft deze nog steeds dezelfde waarde.
        //
        // waarde = -100
        assertEquals(expected, account.getId());
        
        // Bij de flush wordt alle veranderingen naar de database geschreven,
        // dus voor de commit, middels de volgende statements:
        //      INSERT INTO ACCOUNT (ACCOUNTNR, BALANCE, THRESHOLD) VALUES (111, 0, 0)
        //      SELECT LAST_INSERT_ID()
        em.flush();
        
        // Na de flush wordt het Id nummer gelijk aan returnde id nummer van de statement.
        //
        // waarde = 1
        assertNotEquals(expected, account.getId());
        
        em.getTransaction().commit();
        
        // EINDRESULTAAT:
        // In de database staat nu een rij in de tabel account met de waardes:
        //      ID = 1, ACCOUNTNR = 111, BALANCE = 0, THRESHOLD = 0
    }
    
    // OPDRACHT 4
    
    @Test
    public void changesTest()
    {
        Long expectedBalance = 400L;
        Account account = new Account(114L);
        
        System.out.println("--- START CHANGES TEST");
        
        em.getTransaction().begin();
        em.persist(account);
        account.setBalance(expectedBalance);
        
        // Bij de commit worden de volgende statements uitgevoerd:
        //      INSERT INTO ACCOUNT (ACCOUNTNR, BALANCE, THRESHOLD) VALUES (114, 400, 0)
        //      SELECT LAST_INSERT_ID()
        em.getTransaction().commit();
        
        // Balans zou nog steeds dezelfde waarde moeten hebben.
        //
        // waarde = 400
        assertEquals(expectedBalance, account.getBalance());
        
        EntityManager em2 = emf.createEntityManager();
        em2.getTransaction().begin();
        
        // Er wordt gezocht naar de rij de we net hebben toegevoegd middels de
        // volgende statement:
        //      SELECT ID, ACCOUNTNR, BALANCE, THRESHOLD FROM ACCOUNT WHERE (ID = 1)
        Account found = em2.find(Account.class, account.getId());
        
        // Het account, en dus de balans, wordt uit de database gehaald en zou dezelfde
        // waarde moeten hebben als die van expectedBalance
        //
        // waarde = 400
        assertEquals(expectedBalance, found.getBalance());
        
        // EINDRESULTAAT:
        // In de database staat nu een rij in de tabel account met de waardes:
        //      ID = 1, ACCOUNTNR = 114, BALANCE = 400, THRESHOLD = 0
    }
    
    // OPDRACHT 5
    
    @Test
    public void refreshTest()
    {
        Long expectedBalance = 400L;
        Account account1 = new Account(114L);
        
        System.out.println("--- START REFRESH TEST");
        
        em.getTransaction().begin();
        em.persist(account1);
        account1.setBalance(expectedBalance);
        
        // Bij de commit worden de volgende statements uitgevoerd:
        //      INSERT INTO ACCOUNT (ACCOUNTNR, BALANCE, THRESHOLD) VALUES (114, 400, 0)
        //      SELECT LAST_INSERT_ID()
        em.getTransaction().commit();
        
        // Balans zou nog steeds dezelfde waarde moeten hebben.
        //
        // waarde = 400
        assertEquals(expectedBalance, account1.getBalance());

        EntityManager em2 = emf.createEntityManager();
        em2.getTransaction().begin();
        
        // Er wordt gezocht naar de rij de we net hebben toegevoegd middels de
        // volgende statement:
        //      SELECT ID, ACCOUNTNR, BALANCE, THRESHOLD FROM ACCOUNT WHERE (ID = 2)
        Account account2 = em2.find(Account.class, account1.getId());
        
        // Het account, en dus de balans, wordt uit de database gehaald en zou dezelfde
        // waarde moeten hebben als die van expectedBalance
        //
        // waarde = 400
        assertEquals(expectedBalance, account2.getBalance());
        
        em2.persist(account2);
        account2.setBalance(account2.getBalance() / 2);
        
        // Door de commit wordt de volgende statement uitgevoerd om de balans te wijzigen:
        //      UPDATE ACCOUNT SET BALANCE = 200 WHERE (ID = 2)
        em2.getTransaction().commit();
        
        // We refreshen account1, deze wordt dan opnieuw opgehaald uit de database middels
        // volgende statement:
        //      SELECT ID, ACCOUNTNR, BALANCE, THRESHOLD FROM ACCOUNT WHERE (ID = 2)
        em.refresh(account1);
        
        // Balans van account1 en account 2 zou nu dan ook gelijk moeten zijn
        //
        // waarde = true
        assertEquals(account1.getBalance(), account2.getBalance());
        
        // EINDRESULTAAT:
        // In de database staat nu een rij in de tabel account met de waardes:
        //      ID = 2, ACCOUNTNR = 114, BALANCE = 200, THRESHOLD = 0
    }
    
    // OPDRACHT 6
    
    @Test
    public void mergeTest()
    {
        Account acc  = new Account(1L);
        Account acc2 = new Account(2L);
        Account acc9 = new Account(9L);

        AccountDAOJPAImpl accountDAO = new AccountDAOJPAImpl(em);
        
        System.out.println("--- START MERGE TEST SCENRARIO 1");
        
        // scenario 1
        Long balance1 = 100L;
        em.getTransaction().begin();
        em.persist(acc);
        acc.setBalance(balance1);
        
        // We controlleren de opgegeven balans
        //
        // waarde = 100
        assertEquals(acc.getBalance(), balance1);
        
        // Door de commit op deze transactie voeren we de volgende statements uit:
        //      INSERT INTO ACCOUNT (ACCOUNTNR, BALANCE, THRESHOLD) VALUES (1, 100, 0)
        //      SELECT LAST_INSERT_ID()
        em.getTransaction().commit();
        
        // Voor de zekerheid doen we een refresh en wordt de volgende statement uitgevoerd:
        //      SELECT ID, ACCOUNTNR, BALANCE, THRESHOLD FROM ACCOUNT WHERE (ID = 5)
        em.refresh(acc);
        
        // Balans uit het opgehaalde object zou gelijk moeten zijn aan balance1 (200)
        //
        // waarde = true
        assertEquals(acc.getBalance(), balance1);
    
        System.out.println("--- START MERGE TEST SCENRARIO 2");
         
        // scenario 2
        Long balance2a = 211L;
        acc = new Account(2L);
        em.getTransaction().begin();
        
        // Controlleer account nummer van acc9, deze zou niet gelijk moeten
        // zijn aan die van acc.
        //
        // waarde = false
        assertNotEquals(acc.getAccountNr(), acc9.getAccountNr());
        acc9 = em.merge(acc);

        // Door de merge zou acc9 het account nummer van acc over moeten nemen
        //
        // waarde = true
        assertEquals(acc.getAccountNr(), acc9.getAccountNr());
        
        acc.setBalance(balance2a);
        acc9.setBalance(balance2a+balance2a);
        
        // Door de commit op deze transactie voeren we de volgende statements uit:
        //      INSERT INTO ACCOUNT (ACCOUNTNR, BALANCE, THRESHOLD) VALUES (2, 422, ?)
        //      SELECT LAST_INSERT_ID()
        em.getTransaction().commit();

        
        
        // Voor het zoeken naar een account door middel van account nummer wordt de volgende SQL
        // statement gebruikt (zoals gedefinieerd in Account.java):
        //      select a from Account as a where a.accountNr = :accountNr 
        //
        // waarde = null (throws NoResultException in getSingleResult())
        Account found = accountDAO.findByAccountNr(acc.getAccountNr()); 
        
        // Aangezien acc9 het managed object is, zal deze balans gelijk moeten zijn
        // met de balans in de database
        //
        // waarde = tue
        assertEquals(found.getBalance(), acc9.getBalance());
        
        System.out.println("--- START MERGE TEST SCENARIO 3");
        
         // scenario 3
        Long balance3b = 322L;
        Long balance3c = 333L;
        acc = new Account(3L);
        em.getTransaction().begin();
        acc2 = em.merge(acc);
        
        // Aangezien acc niet het managed object is, maar alleen gebruikt wordt
        // om te mergen bestaat deze niet in de enitymanager
        //
        // waarde = false
        assertFalse(em.contains(acc));
        
        // Aangezien acc2 het gemanaged object is, dat aangemaakt wordt middels de merge,
        // bestaat deze wel in de entitymanager
        //
        // waarde = true
        assertTrue(em.contains(acc2));
        
        // Objecten acc en acc2 zijn niet gelijk aan elkaar. Het acc2 object wordt door
        // de merge opnieuw aangemaakt.
        // 
        // waarde = false
        assertNotEquals(acc,acc2); 
        
        acc2.setBalance(balance3b);
        acc.setBalance(balance3c);
        
        // Door de commit op deze transactie voeren we de volgende statements uit:
        //      INSERT INTO ACCOUNT (ACCOUNTNR, BALANCE, THRESHOLD) VALUES (3, 322, 0)
	//      SELECT LAST_INSERT_ID()
        em.getTransaction().commit();
        
        // Voor het zoeken naar een account door middel van account nummer wordt de volgende SQL
        // statement gebruikt (zoals gedefinieerd in Account.java):
        //      select a from Account as a where a.accountNr = :accountNr 
        //
        // waarde = null (throws NoResultException in getSingleResult())
        found = accountDAO.findByAccountNr(acc.getAccountNr()); 
        
        // Balans in de database zou balance3b moeten zijn doordat de balans van acc2
        // hiermee geset wordt.
        //
        // waarde = tue
        assertEquals(found.getBalance(), balance3b);
       
        System.out.println("--- START MERGE TEST SCENARIO 4");
         
        // scenario 4
        Account account = new Account(114L);
        account.setBalance(450L);
        em.getTransaction().begin();
        em.persist(account);
        
        // Door de commit worden de volgende statements uitgevoerd:
        //      INSERT INTO ACCOUNT (ACCOUNTNR, BALANCE, THRESHOLD) VALUES (114, 450, ?)
	//      SELECT LAST_INSERT_ID()
        em.getTransaction().commit();
        
       
        Account account2 = new Account(114L);
        Account tweedeAccountObject = account2;
        tweedeAccountObject.setBalance(650l);
        
        // Aangezien het tweedeAccountObject gelijk is aan account2
        // (is een referentie naar het object) wordt de balans ook gewijzigd bij 
        // account2 indien deze gewijzigd wordt bij tweedeAccountObject.
        assertEquals((Long)650L,account2.getBalance());  //verklaar
        
        account2.setId(account.getId());
        em.getTransaction().begin();
        account2 = em.merge(account2);
        
        // account2 krijgt eerst het id nummer van account en vervolgens
        // gemerged. Aangezien account al in de entitymanager staat (heeft gelijke id)
        // wordt er een referentie van account geretouneerd en zijn deze objecten dus 
        // gelijk.
        assertSame(account,account2); 
        
        // Aangezien deze managed wordt door de merge bestaat deze in de entitymanager
        assertTrue(em.contains(account2)); 
        
        // Het tweedeAccountObject is geen referentie naar account2 meer door
        // de merge en is niet managed.
        assertFalse(em.contains(tweedeAccountObject));  
        tweedeAccountObject.setBalance(850l);
        
        // Doordat het tweedeAccountObject niet managed is, wordt de prijs niet aangepast
        // bij account en account2 en blijven deze gelijk
        assertEquals((Long)650L,account.getBalance());  
        assertEquals((Long)650L,account2.getBalance()); 
        
        // Door de commit wordt het volgende statement uitgevoerd: 
        //      UPDATE ACCOUNT SET BALANCE = 650 WHERE (ID = 4)

        em.getTransaction().commit();
        em.close();
        
        // EINDRESULTAAT:
        // In de database staan nu 4 rijen in de tabel account met de waardes:
        //      ID = 1, ACCOUNTNR = 1, BALANCE = 100, THRESHOLD = 0
        //      ID = 2, ACCOUNTNR = 2, BALANCE = 422, THRESHOLD = 0
        //      ID = 3, ACCOUNTNR = 3, BALANCE = 322, THRESHOLD = 0
        //      ID = 4, ACCOUNTNR = 114, BALANCE = 650, THRESHOLD = 0
    }
    
    // OPDRACHT 7 
    
    @Test
    public void findClearTest()
    {
        Account acc1 = new Account(77L);
        em.getTransaction().begin();
        em.persist(acc1);
        
        // Door de commit worden de volgende statements uitgevoerd:
        //      INSERT INTO ACCOUNT (ACCOUNTNR, BALANCE, THRESHOLD) VALUES (77, 0, 0)
	//      SELECT LAST_INSERT_ID()
        em.getTransaction().commit();

        // scenario 1        
        Account accF1;
        Account accF2;
        accF1 = em.find(Account.class, acc1.getId());
        accF2 = em.find(Account.class, acc1.getId());
        
        // Aangezien het bij beide accounts om de entity gaat met het zelfde
        // id nummer wordt door de enititymanager dezelfde referentie naar het object 
        // geretouneerd en zijn deze objecten gelijk.
        assertSame(accF1, accF2);

        // scenario 2        
        accF1 = em.find(Account.class, acc1.getId());
        em.clear();
        accF2 = em.find(Account.class, acc1.getId());
        
        // Aangezien de entitymanager wordt gecleared worden alle entities verwijdert.
        // Doordat deze verwijdert zijn moet de tweede find call een nieuw object aanmaken
        // en zijn deze dus geen referenties naar elkaar
        assertNotSame(accF1, accF2);
        
        // EINDRESULTAAT:
        // In de database staat nu een rij in de tabel account met de waardes:
        //      ID = 1, ACCOUNTNR = 77, BALANCE = 0, THRESHOLD = 0
    }
    
    // OPDRACHT 8
    
    @Test
    public void removeTest()
    {
        Account acc1 = new Account(88L);
        
        System.out.println("--- START REMOVE TEST");
        
        em.getTransaction().begin();
        em.persist(acc1);
        
        // Door de commit worden de volgende statements uitgevoerd:
        //      INSERT INTO ACCOUNT (ACCOUNTNR, BALANCE, THRESHOLD) VALUES (88, 0, 0)
	//      SELECT LAST_INSERT_ID()
        em.getTransaction().commit();
        Long id = acc1.getId();
       
        // Door de remove wordt de volgende statement uitgevoerd:
        //      DELETE FROM ACCOUNT
        em.remove(acc1);
        
        // Doordat er nog een referentie bestaat buiten de entitymanager wordt het
        // object alleen uit de lijst gehaald en kan dus nog steeds gebruikt worden.
        assertEquals(id, acc1.getId());        
        
        Account accFound = em.find(Account.class, id);
        
        // Aangezien het object uit de entitymanager verwijdert wordt deze null;
        assertNull(accFound);
        
        // EINDRESULTAAT:
        // Er staan geen rijen in de database
    }
    
    // OPDRACHT 9

    // GenerationType.TABLE: een sequence table wordt aangemaakt waarmee unieke
    // id nummers worden aangemaakt. Hierbij staat een counter die iedere keer als
    // er een id aangemaakt moet worden opgehoogd wordt. Door deze generation te gebruiken
    // faalt de test uit opdracht 1, omdat er na de persist functie al een id nummer is aangemaakt.
    
    //  GenerationType.SEQUENCE: maakt gebruik van een sequence object bij het genereren
    // van id nummers. Deze wordt door de meeste databases niet ondersteund. De test uit opdracht 1
    // wordt wel goed uitgevoerd, maar aangezien mysql niet tussen de ondersteunde databases staat
    // ben ik er niet zeker van of deze ook daadwerkelijk dit object gebruikt, of een fallback naar
    // IDENTITY heeft.
}
