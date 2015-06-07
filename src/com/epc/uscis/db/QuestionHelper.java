package com.epc.uscis.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class QuestionHelper extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME = "uscis.db";
    
    /** 
     * Points to the latest version of the database. Whenever a 
     * new version of the database is made (i.e. schema change, or
     * updating of some data) this value should be increased.
     */
    private static final int SCHEMA_VERSION = 2;
    
    public QuestionHelper(Context context)
    {
        super(context, DATABASE_NAME, null, SCHEMA_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        // Create the 3 main tables and then populate them with 
        // the appropriate data
        db.execSQL("CREATE TABLE categories(_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, category_name TEXT NOT NULL);");
        db.execSQL("CREATE TABLE sections(_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, category_id INTEGER NOT NULL, section_name TEXT NOT NULL);");
        db.execSQL("CREATE TABLE questions(_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, section_id INTEGER NOT NULL, question TEXT NOT NULL, answer TEXT NOT NULL);");
        
        // This table will hold questions that will be
        // flagged by users (i.e. questions they need
        // to review)
        db.execSQL("CREATE TABLE flagged_questions(_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, question_id INTEGER NOT NULL);");
        
        db.execSQL("INSERT INTO 'categories' VALUES(1,'American Government');");
        db.execSQL("INSERT INTO 'categories' VALUES(2,'American History');");
        db.execSQL("INSERT INTO 'categories' VALUES(3,'Integrated Civics');");
        
        db.execSQL("INSERT INTO 'sections' VALUES(1,1,'Principles of American Democracy');");
        db.execSQL("INSERT INTO 'sections' VALUES(2,1,'System of Government');");
        db.execSQL("INSERT INTO 'sections' VALUES(3,1,'Rights and Responsibilites');");
        db.execSQL("INSERT INTO 'sections' VALUES(4,2,'Colonial Period and Independence');");
        db.execSQL("INSERT INTO 'sections' VALUES(5,2,'1800s'); ");
        db.execSQL("INSERT INTO 'sections' VALUES(6,2,'Recent American History and Other Important Historical Facts');");
        db.execSQL("INSERT INTO 'sections' VALUES(7,3,'Geography');");
        db.execSQL("INSERT INTO 'sections' VALUES(8,3,'Symbols');");
        db.execSQL("INSERT INTO 'sections' VALUES(9,3,'Holidays');");

        // I had to resort to manually inserting the data for all 100
        // questions into the database. I had to use one execSQL method
        // call for each question as calling this method once with a 
        // concatenated string did not work.
        db.execSQL("INSERT INTO 'questions' VALUES(1,1,'What is the supreme law of the land?','The Constitution.');");
        db.execSQL("INSERT INTO 'questions' VALUES(2,1,'What does the Constitution do?','Sets up the government.<br />Defines the government.<br />Protects the basic rights of Americans.');");
        db.execSQL("INSERT INTO 'questions' VALUES(3,1,'The idea of self-government is in the first three words of the Constitution. What are these words?','We the People.');");
        db.execSQL("INSERT INTO 'questions' VALUES(4,1,'What is an amendment?','A change (to the Constitution).<br />An addition (to the Constitution).');");
        db.execSQL("INSERT INTO 'questions' VALUES(5,1,'What do we call the first ten amendments to the Constitution?','The Bill of Rights.');");
        db.execSQL("INSERT INTO 'questions' VALUES(6,1,'What is <u>one</u> right or freedom from the First Amendment?*','Speech.<br/>Religion.<br/>Assembly.<br/>Press.<br/>Petition the Government.');");
        db.execSQL("INSERT INTO 'questions' VALUES(7,1,'How many amendments does the Constitution have?','Twenty-Seven (27).');");
        db.execSQL("INSERT INTO 'questions' VALUES(8,1,'What did the Declaration of Independence do?','Announced our independence (from Great Britain).<br />Declared our independence (from Great Britain).<br />Said that the United States is free (from Great Britain).');");
        db.execSQL("INSERT INTO 'questions' VALUES(9,1,'What are <u>two</u> rights in the Declaration of Independence?','Life.<br />Liberty.<br />Pursuit of Happiness.');");
        db.execSQL("INSERT INTO 'questions' VALUES(10,1,'What is freedom of religion?','You can practice any religion, or not practice a religion.'); ");
        db.execSQL("INSERT INTO 'questions' VALUES(11,1,'What is the economic system in the United States?*','Capitalist economy.<br />Market economy.');");
        db.execSQL("INSERT INTO 'questions' VALUES(12,1,'What is the \"rule of law\"?','Everyone must follow the law.<br />Leaders must obey the law.<br />Government must obey the law.<br />No one is above the law.');");
        db.execSQL("INSERT INTO 'questions' VALUES(13,2,'Name <u>one</u> branch or part of the government.*','Congress.<br />Legislative.<br />President.<br />Executive.<br />The Courts.<br />Judicial.');");
        db.execSQL("INSERT INTO 'questions' VALUES(14,2,'What stops <u>one</u> branch of government from becoming too powerful?','Checks and balances.<br />Seperation of powers.');");
        db.execSQL("INSERT INTO 'questions' VALUES(15,2,'Who is in charge of the executive branch?','The President.'); ");
        db.execSQL("INSERT INTO 'questions' VALUES(16,2,'Who makes federal laws?','Congress.<br />Senate and House (of Representatives).<br />(U.S. Or national) Legislature.');");
        db.execSQL("INSERT INTO 'questions' VALUES(17,2,'What are the <u>two</u> parts of the U.S. Congress?*','The Senate and House (of Representatives).');");
        db.execSQL("INSERT INTO 'questions' VALUES(18,2,'How many U.S. Senators are there?','One Hundred (100).');");
        db.execSQL("INSERT INTO 'questions' VALUES(19,2,'We elect a U.S. Senator for how many years?','Six (6).');");
        db.execSQL("INSERT INTO 'questions' VALUES(20,2,'What is <u>one</u> of your state''s U.S. Senators now?*','Answers will vary (District of Columbia residents and residents of U.S. Territories should answer that D.C. (or the territory where the applicant lives) has no U.S. Senators.)');");
        db.execSQL("INSERT INTO 'questions' VALUES(21,2,'The House of Representatives has how many voting members?','Four Hundred Thirty-Five (435).');");
        db.execSQL("INSERT INTO 'questions' VALUES(22,2,'We elect a U.S. Representative for how many years?','Two (2).');");
        db.execSQL("INSERT INTO 'questions' VALUES(23,2,'Name your U.S. Representative.','Answers will vary (Residents of territories with nonvoting Delegates or Resident Commissioners may provide the name of that Delegate or Commissioner, also acceptable is any statement that the territory has no (voting) Representative in Congress.)');");
        db.execSQL("INSERT INTO 'questions' VALUES(24,2,'Who does a U.S. Senator represent?','All the People of the State.');");
        db.execSQL("INSERT INTO 'questions' VALUES(25,'2','Why do some states have more Representatives than others?','(because of) The state''s population.<br/>(because) They have more people.<br />(because) Some states have more people.');");
        db.execSQL("INSERT INTO 'questions' VALUES(26,2,'We elect a President for how many years?','Four (4).');");
        db.execSQL("INSERT INTO 'questions' VALUES(27,2,'In what month do we vote for President?*','November.');");
        db.execSQL("INSERT INTO 'questions' VALUES(28,2,'What is the name of the President of the United States now?*','Barack Obama.<br />Obama.');");
        db.execSQL("INSERT INTO 'questions' VALUES(29,2,'What is the name of the Vice President of the United Sates now?','Joseph R. Biden.<br />Joe Biden.<br />Biden.');");
        db.execSQL("INSERT INTO 'questions' VALUES(30,2,'If the President can no longer serve, who becomes President?','The Vice President.');");
        db.execSQL("INSERT INTO 'questions' VALUES(31,2,'If both the President and the Vice President can no longer serve, who becomes the President?','The Speaker of the House.');");
        db.execSQL("INSERT INTO 'questions' VALUES(32,2,'Who is the Commander in Chief of the military?','The President.');");
        db.execSQL("INSERT INTO 'questions' VALUES(33,2,'Who signs bills to become laws?','The President.');");
        db.execSQL("INSERT INTO 'questions' VALUES(34,2,'Who vetoes bills?','The President.');");
        db.execSQL("INSERT INTO 'questions' VALUES(35,2,'What does the President''s Cabinet do?','Advices the President.');");
        db.execSQL("INSERT INTO 'questions' VALUES(36,2,'What are <u>two</u> Cabinet-level positions?','Secretary of Agriculture.<br/>Secretary of Commerce.<br />Secretary of Defense.<br />Secretary of Education.<br />Secretary of Energy.<br />Secretary of Health and Human Services.<br />Secretary of Homeland Security.<br />Secretary of Housing and Urban Development.<br />Secretary of the Interior.<br/>Secretary of Labor.<br />Secretary of State.<br />Secretary of Transportation.<br />Secretary of the Treasury.<br />Secretary of Veterans Affairs.<br />Attorney General.<br/>Vice President.');");
        db.execSQL("INSERT INTO 'questions' VALUES(37,2,'What does the judicial branch do?','Reviews Laws.<br />Explains Laws.<br />Resolves Disputes (disagreements).<br />Decides if a law goes against the Constitution.');");
        db.execSQL("INSERT INTO 'questions' VALUES(38,2,'What is the highest court in the United States?','The Supreme Court.');");
        db.execSQL("INSERT INTO 'questions' VALUES(39,2,'How many justices are on the Supreme Court?','Nine (9).');");
        db.execSQL("INSERT INTO 'questions' VALUES(40,2,'Who is the Chief Justice of the United States now?','John Roberts (John G. Roberts, Jr.).');");
        db.execSQL("INSERT INTO 'questions' VALUES(41,2,'Under our Constitution, some powers belong to the federal government.  What is <u>one</u> power of the federal government?','To print money.<br />To declare war.<br />To create an army.<br />To make treaties.');");
        db.execSQL("INSERT INTO 'questions' VALUES(42,2,'Under our Constitution, some powers belong to the states.  What is <u>one</u> power of the states?','Provide schooling and education.<br />Provide protection (police).<br />Provide safety (fire departments).<br />Give a driver''s license.<br />Approve zoning and land use.');");
        db.execSQL("INSERT INTO 'questions' VALUES(43,2,'Who is the Governor of your state now?','Answers will vary (District of Columbia residents should answer that D.C. Does not have a Governor.)');");
        db.execSQL("INSERT INTO 'questions' VALUES(44,2,'What is the capital of your state?*','Answers will vary (District of Columbia residents should answer that D.C. Is not a state and does not have a capital, residents of U.S. Territories should name the capital of the territory.)');");
        db.execSQL("INSERT INTO 'questions' VALUES(45,2,'What are <u>two</u> major political parties in the United States?*','Democratic and Republican.');");
        db.execSQL("INSERT INTO 'questions' VALUES(46,2,'What is the political party of the President now?','Democratic (party).');");
        db.execSQL("INSERT INTO 'questions' VALUES(47,2,'What is the name of the Speaker of the House of Representatives now?','(John) Boehner.');");
        db.execSQL("INSERT INTO 'questions' VALUES(48,3,'There are four amendments to the Constitution about who can vote.  Describe <u>one</u> of them.','Citizens eighteen (18) and older (can vote).<br />You don''t have to pay (a poll tax) to vote.<br />Any citizen can vote (women and men can vote).<br />A male citizen of any race (can vote).');");
        db.execSQL("INSERT INTO 'questions' VALUES(49,3,'What is <u>one</u> responsibility that is only for United States citizens?*','Serve on a jury.<br />Vote in a federal election.');");
        db.execSQL("INSERT INTO 'questions' VALUES(50,3,'Name <u>one</u> right only for United States Citizens.','Vote in a Federal Election.<br />Run for Federal Office.');");
        db.execSQL("INSERT INTO 'questions' VALUES(51,3,'What are <u>two</u> rights of everyone living in the United States?','Freedom of Expression.<br />Freedom of Speech.<br />Freedom of Assembly.<br />Freedom to petition the Government.<br />Freedom of worship.<br />The right to bear arms.');");
        db.execSQL("INSERT INTO 'questions' VALUES(52,3,'What do we show loyalty to when we say the Pledge of Allegiance?','The United States.<br />The Flag.');");
        db.execSQL("INSERT INTO 'questions' VALUES(53,3,'What is the <u>one</u> promise you make when you become a United States citizen?','Give up loyalty to other countries.<br />Defend the Constitution and laws of the United States.<br />Obey the laws of the United States.<br />Serve in the U.S. military (if needed).<br />Serve (do important work for) the nation (if needed).<br/>Be loyal to the United States.');");
        db.execSQL("INSERT INTO 'questions' VALUES(54,3,'How old do citizens have to be to vote for President?*','Eighteen (18) and Older.');");
        db.execSQL("INSERT INTO 'questions' VALUES(55,3,'What are <u>two</u> ways that Americans can participate in their democracy?','Vote.<br />Join a political party.<br />Help with a campaign.<br />Join a civic group.<br />Join a community group.<br/>Give an elected official your opinion on an issue.<br />Call Senators and Representatives.<br />Publicly support or oppose an issue or policy.<br />Run for office.<br />Write to a newspaper.');");
        db.execSQL("INSERT INTO 'questions' VALUES(56,3,'When is the last day you can send in federal income tax forms?*','April Fifteen (15).');");
        db.execSQL("INSERT INTO 'questions' VALUES(57,3,'When must all men register for Selective Service?','At age Eighteen (18).<br/>Between Eighteen (18) and Twenty-Six (26) years of age.');");
        db.execSQL("INSERT INTO 'questions' VALUES(58,4,'What is <u>one</u> reason colonists came to America?','Freedom.<br />Political Liberty.<br/>Religious Freedom.<br/>Economic Opportunity.<br/>Practice their religion.<br/>Escape Persecution.');");
        db.execSQL("INSERT INTO 'questions' VALUES(59,4,'Who lived in America before the Europeans arrived?','American Indians.<br/>Native Americans.');");
        db.execSQL("INSERT INTO 'questions' VALUES(60,4,'What group of people was taken to America and sold as slaves?','Africans.<br/>People from Africa.');");
        db.execSQL("INSERT INTO 'questions' VALUES(61,4,'Why did the colonists fight the British?','Because of High Taxes (taxation without representation).<br/>Because the British army stayed in their houses (boarding, quartering).<br/>Because they did not have self-government.');");
        db.execSQL("INSERT INTO 'questions' VALUES(62,4,'Who wrote the Declaration of Independence?','(Thomas) Jefferson.');");
        db.execSQL("INSERT INTO 'questions' VALUES(63,4,'When was the Declaration of Independence adopted?','July Fourth (4), Seventeeen Seventy-Six (1776).');");
        db.execSQL("INSERT INTO 'questions' VALUES(64,4,'There were 13 original states. Name <u>three</u>.','New Hampshire.<br/>Massachusetts.<br/>Rhode Island.<br/>Connecticut.<br/>New York.<br/>New Jersey.<br/>Pennsylvania.<br/>Delaware.<br/>Maryland.<br/>Virginia.<br/>North Carolina.<br/>South Carolina.<br/>Georgia.');");
        db.execSQL("INSERT INTO 'questions' VALUES(65,4,'What happened at the Constitutional Convention?','The Constitution was written.<br/>The Founding Fathers wrote the Constitution.');");
        db.execSQL("INSERT INTO 'questions' VALUES(66,4,'When was the Constitution written?','Seventeen Eighty Seven (1787).');");
        db.execSQL("INSERT INTO 'questions' VALUES(67,4,'The Federalist Papers supported the passage of the U.S. Constitution. Name <u>one</u> of the writers.','(James) Madison.<br/>(Alexander) Hamilton.<br/>(John) Jay.<br/>Publius.');");
        db.execSQL("INSERT INTO 'questions' VALUES(68,4,'What is <u>one</u> thing Benjamin Franklin is famous for?','U.S. Diplomat.<br/>Oldest Member of the Constitutional Convention.<br/>First Postmaster General of the United States.<br/>Writer of \"Poor Richards Almanac\".<br/>Started the first free libraries.');");
        db.execSQL("INSERT INTO 'questions' VALUES(69,4,'Who is the \"Father of Our Country\"?','(George) Washington.');");
        db.execSQL("INSERT INTO 'questions' VALUES(70,4,'Who was the first President?*','(George) Washington.');");
        db.execSQL("INSERT INTO 'questions' VALUES(71,5,'What territory did the United States buy from France in 1803?','The Louisiana Territory.<br/>Louisiana.');");
        db.execSQL("INSERT INTO 'questions' VALUES(72,5,'Name <u>one</u> war fought by the United States in the Eighteen Hundreds (1800''s).','War of Eighteen Twelve (1812).<br/>Mexican-American War.<br/>Civil War.<br/>Spanish-American War.');");
        db.execSQL("INSERT INTO 'questions' VALUES(73,5,'Name the U.S. War between the North and the South.','The Civil War.<br/>The War between the States.');");
        db.execSQL("INSERT INTO 'questions' VALUES(74,5,'Name <u>one</u> problem that led to the Civil War.','Slavery.<br/>Economic Reasons.<br/>States Rights.');");
        db.execSQL("INSERT INTO 'questions' VALUES(75,5,'What was <u>one</u> important thing that Abraham Lincoln did?*','Freed the Slaves (Emancipation Proclamation).<br>Saved (or preserved) the Union.<br/>Led the United States during the Civil War.');");
        db.execSQL("INSERT INTO 'questions' VALUES(76,5,'What did the Emancipation Proclamation do?','Freed the slaves.<br/>Freed slaves in the Confederacy.<br/>Freed slaves in the Confederate states.<br/>Freed slaves in most Southern states.');");
        db.execSQL("INSERT INTO 'questions' VALUES(77,5,'What did Susan B. Anthony do?','Fought for womens right.<br/>Fought for Civil rights.');");
        db.execSQL("INSERT INTO 'questions' VALUES(78,6,'Name <u>one</u> war fought by the United States in the Nineteen Hundreds (1900''s)*.','Word War One (1).<br/>World War Two (2).<br/>Korean War.<br/>Vietnam War.<br/>(Persian) Gulf War.');");
        db.execSQL("INSERT INTO 'questions' VALUES(79,6,'Who was President during World War 1?','(Woodrow) Wilson.');");
        db.execSQL("INSERT INTO 'questions' VALUES(80,6,'Who was President during the Great Depression and World War 2?','(Franklin) Roosevelt.');");
        db.execSQL("INSERT INTO 'questions' VALUES(81,6,'Who did the United States fight in World War 2?','Japan, Germany, and Italy.');");
        db.execSQL("INSERT INTO 'questions' VALUES(82,6,'Before he was President, Eisenhower was a general. What war was he in?','World War Two (2).');");
        db.execSQL("INSERT INTO 'questions' VALUES(83,6,'During the Cold War, what was the main concern of the United States?','Communism.');");
        db.execSQL("INSERT INTO 'questions' VALUES(84,6,'What movement tried to end racial discrimination?','Civil Rights (movement).');");
        db.execSQL("INSERT INTO 'questions' VALUES(85,6,'What did Martin Luther King, Jr. do?*','Fought for civil rights.<br/>Worked for equality for all Americans.');");
        db.execSQL("INSERT INTO 'questions' VALUES(86,6,'What major event happened on September 11, 2001, in the United States?','Terrorists attacked the United States.');");
        db.execSQL("INSERT INTO 'questions' VALUES(87,6,'Name <u>one</u> American Indian tribe in the United States.','Cherokee.<br/>Navajo.<br/>Sioux.<br/>Chippewa.<br/>Choctaw.<br/>Pueblo.<br/>Apache.<br/>Iroquois.<br/>Creek.<br/>Blackfeet.<br/>Seminole.<br/>Cheyenne.<br/>Arawak.<br/>Shawnee.<br/>Mohegan.<br/>Huron.<br/>Oneida.<br/>Lakota.<br/>Crow.<br/>Teton.<br/>Hopi.<br/>Inuit.');");
        db.execSQL("INSERT INTO 'questions' VALUES(88,7,'Name <u>one</u> of the two longest rivers in the United States.','Missouri (River).<br/>Mississippi (River).');");
        db.execSQL("INSERT INTO 'questions' VALUES(89,7,'What ocean is on the West Coast of the United States?','Pacific (Ocean).');");
        db.execSQL("INSERT INTO 'questions' VALUES(90,7,'What ocean is on the East Coast of the United States?','Atlantic (Ocean).');");
        db.execSQL("INSERT INTO 'questions' VALUES(91,7,'Name <u>one</u> US Territory.','Puerto Rico.<br/>U.S. Virgin Islands.<br/>American Samoa.<br/>Northen Mariana Islands.<br/>Guam.');");
        db.execSQL("INSERT INTO 'questions' VALUES(92,7,'Name <u>one</u> state that borders Canada.','Maine.<br/>New Hampshire.<br/>Vermont.<br/>New York.<br/>Pennsylvania.<br/>Ohio.<br/>Michigan.<br/>Minnesota.<br/>North Dakota.<br/>Montana.<br/>Idaho.<br/>Washington.<br/>Alaska.');");
        db.execSQL("INSERT INTO 'questions' VALUES(93,7,'Name <u>one</u> state that borders Mexico.','California.<br/>Arizona.<br/>New Mexico.<br/>Texas.');");
        db.execSQL("INSERT INTO 'questions' VALUES(94,7,'What is the capital of the United States?*','Washington, D.C.');");
        db.execSQL("INSERT INTO 'questions' VALUES(95,7,'Where is the Statue of Liberty?*','New York (Harbor).<br/>Liberty Island.<br />(Also Acceptable are New Jersey, near New York City, and on the Hudson (River))');");
        db.execSQL("INSERT INTO 'questions' VALUES(96,8,'Why does the flag have 13 stripes?','Because there were thirteen (13) original colonies.<br/>Because the stripes represent the original colonies.');");
        db.execSQL("INSERT INTO 'questions' VALUES(97,8,'Why does the flag have 50 stars?*','Because there is one star for each state.<br/>Because each star represents a state.<br/>Because there are fifty (50) states.');");
        db.execSQL("INSERT INTO 'questions' VALUES(98,8,'What is the name of the national anthem?','The Star-Spangled Banner.');");
        db.execSQL("INSERT INTO 'questions' VALUES(99,9,'When do we celebrate Independence Day?*','July Fourth (4).');");
        db.execSQL("INSERT INTO 'questions' VALUES(100,9,'Name <u>two</u> national U.S. Holidays.','New Year''s Day.<br/>Martin Luther King, Jr. Day.<br/>President''s Day.<br/>Memorial Day.<br/>Independence Day.<br/>Labor Day.<br/>Columbus Day.<br/>Veterans Day.<br/>Thanksgiving.<br/>Christmas.');");
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // Note: the new version will always point to 
        // the SCHEMA_VERSION variable above.
        if(oldVersion == 1 && newVersion == 2)
        {
            updateQuestionsTableForTTS(db);
        }
    }
   
    /**
     * Drops the questions table and re-creates the table. It 
     * then re-populates the questions table with the updated
     * questions so that the speech to text feature works as
     * expected. Although this is highly inefficient, it must
     * be done this way because multiple questions changed and
     * I didn't write down what questions were that changed. 
     * What you see being re-inserted is the same as what's in
     * the onCreate method.
     */
    private void updateQuestionsTableForTTS(SQLiteDatabase db)
    {
        db.execSQL("DROP TABLE questions;");
        db.execSQL("CREATE TABLE questions(_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, section_id INTEGER NOT NULL, question TEXT NOT NULL, answer TEXT NOT NULL);");

        db.execSQL("INSERT INTO 'questions' VALUES(1,1,'What is the supreme law of the land?','The Constitution.');");
        db.execSQL("INSERT INTO 'questions' VALUES(2,1,'What does the Constitution do?','Sets up the government.<br />Defines the government.<br />Protects the basic rights of Americans.');");
        db.execSQL("INSERT INTO 'questions' VALUES(3,1,'The idea of self-government is in the first three words of the Constitution. What are these words?','We the People.');");
        db.execSQL("INSERT INTO 'questions' VALUES(4,1,'What is an amendment?','A change (to the Constitution).<br />An addition (to the Constitution).');");
        db.execSQL("INSERT INTO 'questions' VALUES(5,1,'What do we call the first ten amendments to the Constitution?','The Bill of Rights.');");
        db.execSQL("INSERT INTO 'questions' VALUES(6,1,'What is <u>one</u> right or freedom from the First Amendment?*','Speech.<br/>Religion.<br/>Assembly.<br/>Press.<br/>Petition the Government.');");
        db.execSQL("INSERT INTO 'questions' VALUES(7,1,'How many amendments does the Constitution have?','Twenty-Seven (27).');");
        db.execSQL("INSERT INTO 'questions' VALUES(8,1,'What did the Declaration of Independence do?','Announced our independence (from Great Britain).<br />Declared our independence (from Great Britain).<br />Said that the United States is free (from Great Britain).');");
        db.execSQL("INSERT INTO 'questions' VALUES(9,1,'What are <u>two</u> rights in the Declaration of Independence?','Life.<br />Liberty.<br />Pursuit of Happiness.');");
        db.execSQL("INSERT INTO 'questions' VALUES(10,1,'What is freedom of religion?','You can practice any religion, or not practice a religion.'); ");
        db.execSQL("INSERT INTO 'questions' VALUES(11,1,'What is the economic system in the United States?*','Capitalist economy.<br />Market economy.');");
        db.execSQL("INSERT INTO 'questions' VALUES(12,1,'What is the \"rule of law\"?','Everyone must follow the law.<br />Leaders must obey the law.<br />Government must obey the law.<br />No one is above the law.');");
        db.execSQL("INSERT INTO 'questions' VALUES(13,2,'Name <u>one</u> branch or part of the government.*','Congress.<br />Legislative.<br />President.<br />Executive.<br />The Courts.<br />Judicial.');");
        db.execSQL("INSERT INTO 'questions' VALUES(14,2,'What stops <u>one</u> branch of government from becoming too powerful?','Checks and balances.<br />Seperation of powers.');");
        db.execSQL("INSERT INTO 'questions' VALUES(15,2,'Who is in charge of the executive branch?','The President.'); ");
        db.execSQL("INSERT INTO 'questions' VALUES(16,2,'Who makes federal laws?','Congress.<br />Senate and House (of Representatives).<br />(U.S. Or national) Legislature.');");
        db.execSQL("INSERT INTO 'questions' VALUES(17,2,'What are the <u>two</u> parts of the U.S. Congress?*','The Senate and House (of Representatives).');");
        db.execSQL("INSERT INTO 'questions' VALUES(18,2,'How many U.S. Senators are there?','One Hundred (100).');");
        db.execSQL("INSERT INTO 'questions' VALUES(19,2,'We elect a U.S. Senator for how many years?','Six (6).');");
        db.execSQL("INSERT INTO 'questions' VALUES(20,2,'What is <u>one</u> of your state''s U.S. Senators now?*','Answers will vary (District of Columbia residents and residents of U.S. Territories should answer that D.C. (or the territory where the applicant lives) has no U.S. Senators.)');");
        db.execSQL("INSERT INTO 'questions' VALUES(21,2,'The House of Representatives has how many voting members?','Four Hundred Thirty-Five (435).');");
        db.execSQL("INSERT INTO 'questions' VALUES(22,2,'We elect a U.S. Representative for how many years?','Two (2).');");
        db.execSQL("INSERT INTO 'questions' VALUES(23,2,'Name your U.S. Representative.','Answers will vary (Residents of territories with nonvoting Delegates or Resident Commissioners may provide the name of that Delegate or Commissioner, also acceptable is any statement that the territory has no (voting) Representative in Congress.)');");
        db.execSQL("INSERT INTO 'questions' VALUES(24,2,'Who does a U.S. Senator represent?','All the People of the State.');");
        db.execSQL("INSERT INTO 'questions' VALUES(25,'2','Why do some states have more Representatives than others?','(because of) The state''s population.<br/>(because) They have more people.<br />(because) Some states have more people.');");
        db.execSQL("INSERT INTO 'questions' VALUES(26,2,'We elect a President for how many years?','Four (4).');");
        db.execSQL("INSERT INTO 'questions' VALUES(27,2,'In what month do we vote for President?*','November.');");
        db.execSQL("INSERT INTO 'questions' VALUES(28,2,'What is the name of the President of the United States now?*','Barack Obama.<br />Obama.');");
        db.execSQL("INSERT INTO 'questions' VALUES(29,2,'What is the name of the Vice President of the United Sates now?','Joseph R. Biden.<br />Joe Biden.<br />Biden.');");
        db.execSQL("INSERT INTO 'questions' VALUES(30,2,'If the President can no longer serve, who becomes President?','The Vice President.');");
        db.execSQL("INSERT INTO 'questions' VALUES(31,2,'If both the President and the Vice President can no longer serve, who becomes the President?','The Speaker of the House.');");
        db.execSQL("INSERT INTO 'questions' VALUES(32,2,'Who is the Commander in Chief of the military?','The President.');");
        db.execSQL("INSERT INTO 'questions' VALUES(33,2,'Who signs bills to become laws?','The President.');");
        db.execSQL("INSERT INTO 'questions' VALUES(34,2,'Who vetoes bills?','The President.');");
        db.execSQL("INSERT INTO 'questions' VALUES(35,2,'What does the President''s Cabinet do?','Advices the President.');");
        db.execSQL("INSERT INTO 'questions' VALUES(36,2,'What are <u>two</u> Cabinet-level positions?','Secretary of Agriculture.<br/>Secretary of Commerce.<br />Secretary of Defense.<br />Secretary of Education.<br />Secretary of Energy.<br />Secretary of Health and Human Services.<br />Secretary of Homeland Security.<br />Secretary of Housing and Urban Development.<br />Secretary of the Interior.<br/>Secretary of Labor.<br />Secretary of State.<br />Secretary of Transportation.<br />Secretary of the Treasury.<br />Secretary of Veterans Affairs.<br />Attorney General.<br/>Vice President.');");
        db.execSQL("INSERT INTO 'questions' VALUES(37,2,'What does the judicial branch do?','Reviews Laws.<br />Explains Laws.<br />Resolves Disputes (disagreements).<br />Decides if a law goes against the Constitution.');");
        db.execSQL("INSERT INTO 'questions' VALUES(38,2,'What is the highest court in the United States?','The Supreme Court.');");
        db.execSQL("INSERT INTO 'questions' VALUES(39,2,'How many justices are on the Supreme Court?','Nine (9).');");
        db.execSQL("INSERT INTO 'questions' VALUES(40,2,'Who is the Chief Justice of the United States now?','John Roberts (John G. Roberts, Jr.).');");
        db.execSQL("INSERT INTO 'questions' VALUES(41,2,'Under our Constitution, some powers belong to the federal government.  What is <u>one</u> power of the federal government?','To print money.<br />To declare war.<br />To create an army.<br />To make treaties.');");
        db.execSQL("INSERT INTO 'questions' VALUES(42,2,'Under our Constitution, some powers belong to the states.  What is <u>one</u> power of the states?','Provide schooling and education.<br />Provide protection (police).<br />Provide safety (fire departments).<br />Give a driver''s license.<br />Approve zoning and land use.');");
        db.execSQL("INSERT INTO 'questions' VALUES(43,2,'Who is the Governor of your state now?','Answers will vary (District of Columbia residents should answer that D.C. Does not have a Governor.)');");
        db.execSQL("INSERT INTO 'questions' VALUES(44,2,'What is the capital of your state?*','Answers will vary (District of Columbia residents should answer that D.C. Is not a state and does not have a capital, residents of U.S. Territories should name the capital of the territory.)');");
        db.execSQL("INSERT INTO 'questions' VALUES(45,2,'What are <u>two</u> major political parties in the United States?*','Democratic and Republican.');");
        db.execSQL("INSERT INTO 'questions' VALUES(46,2,'What is the political party of the President now?','Democratic (party).');");
        db.execSQL("INSERT INTO 'questions' VALUES(47,2,'What is the name of the Speaker of the House of Representatives now?','(John) Boehner.');");
        db.execSQL("INSERT INTO 'questions' VALUES(48,3,'There are four amendments to the Constitution about who can vote.  Describe <u>one</u> of them.','Citizens eighteen (18) and older (can vote).<br />You don''t have to pay (a poll tax) to vote.<br />Any citizen can vote (women and men can vote).<br />A male citizen of any race (can vote).');");
        db.execSQL("INSERT INTO 'questions' VALUES(49,3,'What is <u>one</u> responsibility that is only for United States citizens?*','Serve on a jury.<br />Vote in a federal election.');");
        db.execSQL("INSERT INTO 'questions' VALUES(50,3,'Name <u>one</u> right only for United States Citizens.','Vote in a Federal Election.<br />Run for Federal Office.');");
        db.execSQL("INSERT INTO 'questions' VALUES(51,3,'What are <u>two</u> rights of everyone living in the United States?','Freedom of Expression.<br />Freedom of Speech.<br />Freedom of Assembly.<br />Freedom to petition the Government.<br />Freedom of worship.<br />The right to bear arms.');");
        db.execSQL("INSERT INTO 'questions' VALUES(52,3,'What do we show loyalty to when we say the Pledge of Allegiance?','The United States.<br />The Flag.');");
        db.execSQL("INSERT INTO 'questions' VALUES(53,3,'What is the <u>one</u> promise you make when you become a United States citizen?','Give up loyalty to other countries.<br />Defend the Constitution and laws of the United States.<br />Obey the laws of the United States.<br />Serve in the U.S. military (if needed).<br />Serve (do important work for) the nation (if needed).<br/>Be loyal to the United States.');");
        db.execSQL("INSERT INTO 'questions' VALUES(54,3,'How old do citizens have to be to vote for President?*','Eighteen (18) and Older.');");
        db.execSQL("INSERT INTO 'questions' VALUES(55,3,'What are <u>two</u> ways that Americans can participate in their democracy?','Vote.<br />Join a political party.<br />Help with a campaign.<br />Join a civic group.<br />Join a community group.<br/>Give an elected official your opinion on an issue.<br />Call Senators and Representatives.<br />Publicly support or oppose an issue or policy.<br />Run for office.<br />Write to a newspaper.');");
        db.execSQL("INSERT INTO 'questions' VALUES(56,3,'When is the last day you can send in federal income tax forms?*','April Fifteen (15).');");
        db.execSQL("INSERT INTO 'questions' VALUES(57,3,'When must all men register for Selective Service?','At age Eighteen (18).<br/>Between Eighteen (18) and Twenty-Six (26) years of age.');");
        db.execSQL("INSERT INTO 'questions' VALUES(58,4,'What is <u>one</u> reason colonists came to America?','Freedom.<br />Political Liberty.<br/>Religious Freedom.<br/>Economic Opportunity.<br/>Practice their religion.<br/>Escape Persecution.');");
        db.execSQL("INSERT INTO 'questions' VALUES(59,4,'Who lived in America before the Europeans arrived?','American Indians.<br/>Native Americans.');");
        db.execSQL("INSERT INTO 'questions' VALUES(60,4,'What group of people was taken to America and sold as slaves?','Africans.<br/>People from Africa.');");
        db.execSQL("INSERT INTO 'questions' VALUES(61,4,'Why did the colonists fight the British?','Because of High Taxes (taxation without representation).<br/>Because the British army stayed in their houses (boarding, quartering).<br/>Because they did not have self-government.');");
        db.execSQL("INSERT INTO 'questions' VALUES(62,4,'Who wrote the Declaration of Independence?','(Thomas) Jefferson.');");
        db.execSQL("INSERT INTO 'questions' VALUES(63,4,'When was the Declaration of Independence adopted?','July Fourth (4), Seventeeen Seventy-Six (1776).');");
        db.execSQL("INSERT INTO 'questions' VALUES(64,4,'There were 13 original states. Name <u>three</u>.','New Hampshire.<br/>Massachusetts.<br/>Rhode Island.<br/>Connecticut.<br/>New York.<br/>New Jersey.<br/>Pennsylvania.<br/>Delaware.<br/>Maryland.<br/>Virginia.<br/>North Carolina.<br/>South Carolina.<br/>Georgia.');");
        db.execSQL("INSERT INTO 'questions' VALUES(65,4,'What happened at the Constitutional Convention?','The Constitution was written.<br/>The Founding Fathers wrote the Constitution.');");
        db.execSQL("INSERT INTO 'questions' VALUES(66,4,'When was the Constitution written?','Seventeen Eighty Seven (1787).');");
        db.execSQL("INSERT INTO 'questions' VALUES(67,4,'The Federalist Papers supported the passage of the U.S. Constitution. Name <u>one</u> of the writers.','(James) Madison.<br/>(Alexander) Hamilton.<br/>(John) Jay.<br/>Publius.');");
        db.execSQL("INSERT INTO 'questions' VALUES(68,4,'What is <u>one</u> thing Benjamin Franklin is famous for?','U.S. Diplomat.<br/>Oldest Member of the Constitutional Convention.<br/>First Postmaster General of the United States.<br/>Writer of \"Poor Richards Almanac\".<br/>Started the first free libraries.');");
        db.execSQL("INSERT INTO 'questions' VALUES(69,4,'Who is the \"Father of Our Country\"?','(George) Washington.');");
        db.execSQL("INSERT INTO 'questions' VALUES(70,4,'Who was the first President?*','(George) Washington.');");
        db.execSQL("INSERT INTO 'questions' VALUES(71,5,'What territory did the United States buy from France in 1803?','The Louisiana Territory.<br/>Louisiana.');");
        db.execSQL("INSERT INTO 'questions' VALUES(72,5,'Name <u>one</u> war fought by the United States in the Eighteen Hundreds (1800''s).','War of Eighteen Twelve (1812).<br/>Mexican-American War.<br/>Civil War.<br/>Spanish-American War.');");
        db.execSQL("INSERT INTO 'questions' VALUES(73,5,'Name the U.S. War between the North and the South.','The Civil War.<br/>The War between the States.');");
        db.execSQL("INSERT INTO 'questions' VALUES(74,5,'Name <u>one</u> problem that led to the Civil War.','Slavery.<br/>Economic Reasons.<br/>States Rights.');");
        db.execSQL("INSERT INTO 'questions' VALUES(75,5,'What was <u>one</u> important thing that Abraham Lincoln did?*','Freed the Slaves (Emancipation Proclamation).<br>Saved (or preserved) the Union.<br/>Led the United States during the Civil War.');");
        db.execSQL("INSERT INTO 'questions' VALUES(76,5,'What did the Emancipation Proclamation do?','Freed the slaves.<br/>Freed slaves in the Confederacy.<br/>Freed slaves in the Confederate states.<br/>Freed slaves in most Southern states.');");
        db.execSQL("INSERT INTO 'questions' VALUES(77,5,'What did Susan B. Anthony do?','Fought for womens right.<br/>Fought for Civil rights.');");
        db.execSQL("INSERT INTO 'questions' VALUES(78,6,'Name <u>one</u> war fought by the United States in the Nineteen Hundreds (1900''s)*.','Word War One (1).<br/>World War Two (2).<br/>Korean War.<br/>Vietnam War.<br/>(Persian) Gulf War.');");
        db.execSQL("INSERT INTO 'questions' VALUES(79,6,'Who was President during World War 1?','(Woodrow) Wilson.');");
        db.execSQL("INSERT INTO 'questions' VALUES(80,6,'Who was President during the Great Depression and World War 2?','(Franklin) Roosevelt.');");
        db.execSQL("INSERT INTO 'questions' VALUES(81,6,'Who did the United States fight in World War 2?','Japan, Germany, and Italy.');");
        db.execSQL("INSERT INTO 'questions' VALUES(82,6,'Before he was President, Eisenhower was a general. What war was he in?','World War Two (2).');");
        db.execSQL("INSERT INTO 'questions' VALUES(83,6,'During the Cold War, what was the main concern of the United States?','Communism.');");
        db.execSQL("INSERT INTO 'questions' VALUES(84,6,'What movement tried to end racial discrimination?','Civil Rights (movement).');");
        db.execSQL("INSERT INTO 'questions' VALUES(85,6,'What did Martin Luther King, Jr. do?*','Fought for civil rights.<br/>Worked for equality for all Americans.');");
        db.execSQL("INSERT INTO 'questions' VALUES(86,6,'What major event happened on September 11, 2001, in the United States?','Terrorists attacked the United States.');");
        db.execSQL("INSERT INTO 'questions' VALUES(87,6,'Name <u>one</u> American Indian tribe in the United States.','Cherokee.<br/>Navajo.<br/>Sioux.<br/>Chippewa.<br/>Choctaw.<br/>Pueblo.<br/>Apache.<br/>Iroquois.<br/>Creek.<br/>Blackfeet.<br/>Seminole.<br/>Cheyenne.<br/>Arawak.<br/>Shawnee.<br/>Mohegan.<br/>Huron.<br/>Oneida.<br/>Lakota.<br/>Crow.<br/>Teton.<br/>Hopi.<br/>Inuit.');");
        db.execSQL("INSERT INTO 'questions' VALUES(88,7,'Name <u>one</u> of the two longest rivers in the United States.','Missouri (River).<br/>Mississippi (River).');");
        db.execSQL("INSERT INTO 'questions' VALUES(89,7,'What ocean is on the West Coast of the United States?','Pacific (Ocean).');");
        db.execSQL("INSERT INTO 'questions' VALUES(90,7,'What ocean is on the East Coast of the United States?','Atlantic (Ocean).');");
        db.execSQL("INSERT INTO 'questions' VALUES(91,7,'Name <u>one</u> US Territory.','Puerto Rico.<br/>U.S. Virgin Islands.<br/>American Samoa.<br/>Northen Mariana Islands.<br/>Guam.');");
        db.execSQL("INSERT INTO 'questions' VALUES(92,7,'Name <u>one</u> state that borders Canada.','Maine.<br/>New Hampshire.<br/>Vermont.<br/>New York.<br/>Pennsylvania.<br/>Ohio.<br/>Michigan.<br/>Minnesota.<br/>North Dakota.<br/>Montana.<br/>Idaho.<br/>Washington.<br/>Alaska.');");
        db.execSQL("INSERT INTO 'questions' VALUES(93,7,'Name <u>one</u> state that borders Mexico.','California.<br/>Arizona.<br/>New Mexico.<br/>Texas.');");
        db.execSQL("INSERT INTO 'questions' VALUES(94,7,'What is the capital of the United States?*','Washington, D.C.');");
        db.execSQL("INSERT INTO 'questions' VALUES(95,7,'Where is the Statue of Liberty?*','New York (Harbor).<br/>Liberty Island.<br />(Also Acceptable are New Jersey, near New York City, and on the Hudson (River))');");
        db.execSQL("INSERT INTO 'questions' VALUES(96,8,'Why does the flag have 13 stripes?','Because there were thirteen (13) original colonies.<br/>Because the stripes represent the original colonies.');");
        db.execSQL("INSERT INTO 'questions' VALUES(97,8,'Why does the flag have 50 stars?*','Because there is one star for each state.<br/>Because each star represents a state.<br/>Because there are fifty (50) states.');");
        db.execSQL("INSERT INTO 'questions' VALUES(98,8,'What is the name of the national anthem?','The Star-Spangled Banner.');");
        db.execSQL("INSERT INTO 'questions' VALUES(99,9,'When do we celebrate Independence Day?*','July Fourth (4).');");
        db.execSQL("INSERT INTO 'questions' VALUES(100,9,'Name <u>two</u> national U.S. Holidays.','New Year''s Day.<br/>Martin Luther King, Jr. Day.<br/>President''s Day.<br/>Memorial Day.<br/>Independence Day.<br/>Labor Day.<br/>Columbus Day.<br/>Veterans Day.<br/>Thanksgiving.<br/>Christmas.');");
    }
   
    /**
     * Allows you to select the _id, question and answer from the 
     * questions table. 
     * @param orderBy - Name of Column to use to sort list of question
     * @return
     */
    public Cursor getAllQuestions(String orderBy)
    {
        return(getReadableDatabase().rawQuery("SELECT _id, question, answer FROM questions ORDER BY " + orderBy, null));
    }
   
    /**
     * This method will return all Questions in a random
     * order.
     * @return
     */
    public Cursor getAllRandomQuestions()
    {
        return(getReadableDatabase().rawQuery("SELECT _id, question, answer FROM questions ORDER BY RANDOM()", null));
    }
   
    /**
     * Allows a user to select a chosen number of questions
     * to study
     * @param numberOfQuestions
     * @return
     */
    public Cursor getRandomQuestions(int numberOfQuestions)
    {
        return(getReadableDatabase().rawQuery("SELECT _id, question, answer FROM questions ORDER BY RANDOM() Limit " + numberOfQuestions, null));
    }
  
    /**
     * This method allows a user to get 10 random 
     * questions.
     * @return
     */
    public Cursor getTenRandomQuestions()
    {
        return(getReadableDatabase().rawQuery("SELECT _id, question, answer FROM questions ORDER BY RANDOM() Limit 10", null));
    }
   
    /**
     * Returns a specific question based on the question id 
     * (a.k.a. the question number) passed to this function.
     * @param questionId
     * @return
     */
    public Cursor getQuestionById(int questionId)
    {
        String[] args = {Integer.toString(questionId)};
        return getReadableDatabase().rawQuery("SELECT _id, question, answer FROM questions WHERE _id = ?", args);
    }
   
    /**
     * Generates an int array with integer values
     * ranging from 1 to numberOfQuestions that will be used to
     * get questions in a random order.
     * @param numberOfQuestions - The number of questions to return.  This
     * value must be between 1 and 100
     * @return
     */
    public int[] randomIntArray(int numberOfQuestions)
    {
        int[] questionIdArray = new int[numberOfQuestions];
        Cursor cursor = getReadableDatabase().rawQuery("SELECT _id FROM questions ORDER BY RANDOM() LIMIT " + numberOfQuestions + " ", null);
        cursor.moveToFirst();
        int index = 0;
        
        while(cursor.isAfterLast() == false)
        {
            questionIdArray[index] = cursor.getInt(0);
           
            // After we have reached the end of the array
            // we have to get out of this while loop
            if(index == numberOfQuestions - 1)
            {
                break;
            }
            
            cursor.moveToNext();
            index++; 
        }
        
        cursor.close();
        return questionIdArray;
    }
   
    /**
     * This method will retrieve all of the questions (in an int array) based
     * on what category the questions belong to.
     * @param categoryId - the Unique Category ID used to
     * get all the questions in this category
     * @return Integer Array with all the questions that belong
     * to the Category 
     */
    public int[] questionsArrayByCategory(int categoryId)
    {
        String[] args = {Integer.toString(categoryId)};
        String sqlQuery = "SELECT _id FROM questions WHERE section_id IN " +
                          "(SELECT _id FROM sections WHERE category_id = ?) ";
        Cursor cursor = getReadableDatabase().rawQuery(sqlQuery, args);
       
        // Initialize the array with the number of questions that 
        // have been found by the query above
        int[] questionsArray = new int[cursor.getCount()];
        int numberOfQuestions = questionsArray.length - 1;
        
        // Now we'll iterate thru each question and populate the
        // integer array accordingly
        cursor.moveToFirst();
        int index = 0;
        while(cursor.isAfterLast() == false)
        {
            questionsArray[index] = cursor.getInt(0);
            
            if(index == numberOfQuestions)
            {
                break;
            }
            
            cursor.moveToNext();
            index++;
        }
        
        return questionsArray;
    }
   
    /**
     * Returns the lists or questions for a specified category.
     * @param categoryId
     * @return
     */
    public Cursor getQuestionsByCategory(int categoryId)
    {
        String[] args = {Integer.toString(categoryId)};
        return getReadableDatabase().rawQuery("SELECT _id, question, answer FROM questions WHERE section_id IN " +
                                              "(SELECT _id FROM sections WHERE category_id = ?) ", args);
    }

    /**
     * Returns one random question from the database.
     * @return
     */
    public Cursor getRandomQuestion()
    {
        return getReadableDatabase().rawQuery("SELECT _id, question, answer FROM questions ORDER BY RANDOM() Limit 1", null);
    }
    
    public String getQuestionNumber(Cursor c)
    {
        return(c.getString(0));
    }
    
    public String getQuestion(Cursor c)
    {
        return(c.getString(1));
    }
    
    public String getAnswer(Cursor c)
    {
        return(c.getString(2));
    }
   
    /**
     * Returns a Cursor that contains all flagged questions.
     * @return Cursor to iterate thru questions that have 
     * been flagged. 
     */
    public Cursor getAllFlaggedQuestions()
    {
        return getReadableDatabase().rawQuery("SELECT _id, question, answer FROM questions WHERE _id IN (SELECT question_id FROM flagged_questions) ORDER BY RANDOM()", null);
    }
   
    /**
     * Get all Flagged Questions that currently exist in the
     * flagged_questions table.
     * @return - array of integers containing all questions currently
     * flagged.
     */
    public int[] getAllFlaggedQuestionsIntArray()
    {
        Cursor cursor = getReadableDatabase().rawQuery("SELECT question_id FROM flagged_questions ORDER BY RANDOM()", null);
        
        int[] questionArray = new int[cursor.getCount()];
        int numberOfQuestions = questionArray.length - 1;
        int index = 0;
        cursor.moveToFirst();
        
        while(cursor.isAfterLast() == false)
        {
            questionArray[index] = cursor.getInt(0);
            
            if(index == numberOfQuestions)
            {
                break;
            }
            
            cursor.moveToNext();
            index++;
        }
        
        cursor.close();
        return questionArray;
    }
   
    /**
     * Inserts a question into the flagged_questions table.
     * These are questions that user needs to review for his/her
     * US Citizenship test
     * @param questionNumber - Question Number to Flag
     */
    public void insertFlaggedQuestion(int questionNumber)
    {
        ContentValues values = new ContentValues();
        values.put("question_id", questionNumber);
        getWritableDatabase().insert("flagged_questions", null, values);
    }
   
    /**
     * Tests to see if a question is already flagged. If this
     * evaluates to true, then the question is already flagged
     * and thus the question should not be re-inserted into the 
     * flagged_questions table.  If this evaluates to false, then
     * the question has not yet been flagged and therefore the
     * question should be inserted into the flagged_questions 
     * table.
     * @param questionNumber - the Question Number to check 
     * @return Returns true or false depending on whether or not
     * the question already exists in the flagged_questions table.
     */
    public boolean isFlaggedQuestion(int questionNumber)
    {
        String[] args = {Integer.toString(questionNumber)};
        Cursor cursor = getReadableDatabase().rawQuery("SELECT COUNT(_id) FROM flagged_questions WHERE question_id = ?", args); 
        cursor.moveToFirst();
        int numberOfQuestions = cursor.getInt(0);
        cursor.close();
       
        // If the question is already flagged this will
        // evaluate to true, otherwise this will evaluate
        // to false.
        return (numberOfQuestions == 1);
    }
   
    /**
     * This will remove the question number that is currently
     * flagged in the flagged_question table.
     * @param questionNumber - The question number to remove from
     * the flagged_question table.
     */
    public void removeFlaggedQuestion(int questionNumber)
    {
        String[] whereArgs = {Integer.toString(questionNumber)};
        getWritableDatabase().delete("flagged_questions", "question_id = ?", whereArgs);
    }
   
    /**
     * This will clear out (i.e. remove) all Flagged Questions
     * that the user has accumulated for his/her own studies.
     */
    public void clearAllFlaggedQuestions()
    {
        getWritableDatabase().delete("flagged_questions", null, null);
    }
    
    public int getFlaggedQuestionsCount()
    {
        return (int)DatabaseUtils.queryNumEntries(getReadableDatabase(), "flagged_questions");
    }
}
