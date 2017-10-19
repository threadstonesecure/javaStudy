-- 全文索引[MED2.IDXFT_BIGTABLE_CHEM_NAME]创建脚本如下：
begin
  ctx_ddl.create_preference('"IDXFT_BIGTABLE_CHEM_NAME_LEX"','CHINESE_VGRAM_LEXER');
end;
/

begin
  ctx_ddl.create_preference('"IDXFT_BIGTABLE_CHEM_NAME_WDL"','BASIC_WORDLIST');
  ctx_ddl.set_attribute('"IDXFT_BIGTABLE_CHEM_NAME_WDL"','SUBSTRING_INDEX','YES');
  ctx_ddl.set_attribute('"IDXFT_BIGTABLE_CHEM_NAME_WDL"','PREFIX_INDEX','YES');
end;
/

begin
  ctx_ddl.create_stoplist('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','BASIC_STOPLIST');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','Mr');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','Mrs');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','Ms');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','a');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','all');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','almost');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','also');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','although');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','an');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','and');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','any');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','are');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','as');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','at');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','be');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','because');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','been');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','both');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','but');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','by');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','can');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','could');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','d');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','did');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','do');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','does');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','either');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','for');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','from');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','had');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','has');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','have');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','having');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','he');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','her');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','here');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','hers');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','him');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','his');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','how');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','however');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','i');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','if');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','in');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','into');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','is');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','it');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','its');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','just');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','ll');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','me');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','might');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','my');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','no');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','non');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','nor');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','not');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','of');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','on');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','one');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','only');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','onto');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','or');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','our');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','ours');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','s');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','shall');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','she');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','should');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','since');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','so');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','some');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','still');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','such');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','t');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','than');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','that');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','the');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','their');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','them');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','then');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','there');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','therefore');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','these');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','they');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','this');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','those');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','though');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','through');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','thus');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','to');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','too');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','until');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','ve');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','very');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','was');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','we');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','were');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','what');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','when');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','where');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','whether');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','which');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','while');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','who');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','whose');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','why');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','will');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','with');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','would');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','yet');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','you');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','your');
  ctx_ddl.add_stopword('"IDXFT_BIGTABLE_CHEM_NAME_SPL"','yours');
end;
/

begin
  ctx_ddl.create_preference('"IDXFT_BIGTABLE_CHEM_NAME_STO"','BASIC_STORAGE');
  ctx_ddl.set_attribute('"IDXFT_BIGTABLE_CHEM_NAME_STO"','I_TABLE_CLAUSE','tablespace users');
  ctx_ddl.set_attribute('"IDXFT_BIGTABLE_CHEM_NAME_STO"','K_TABLE_CLAUSE','tablespace users');
  ctx_ddl.set_attribute('"IDXFT_BIGTABLE_CHEM_NAME_STO"','R_TABLE_CLAUSE','tablespace users');
  ctx_ddl.set_attribute('"IDXFT_BIGTABLE_CHEM_NAME_STO"','N_TABLE_CLAUSE','tablespace users');
  ctx_ddl.set_attribute('"IDXFT_BIGTABLE_CHEM_NAME_STO"','I_INDEX_CLAUSE','tablespace users');
end;
/

begin
  ctx_ddl.create_index_set('"IDXFT_BIGTABLE_CHEM_NAME_IXS"');
end;
/


begin
  ctx_output.start_log('IDXFT_BIGTABLE_CHEM_NAME_LOG');
end;
/

create index "MED2"."IDXFT_BIGTABLE_CHEM_NAME"
  on "MED2"."BIGTABLE"
      ("DRUG_CHEM_NAME")
  indextype is ctxsys.ctxcat
  parameters('
    lexer           "IDXFT_BIGTABLE_CHEM_NAME_LEX"
    wordlist        "IDXFT_BIGTABLE_CHEM_NAME_WDL"
    stoplist        "IDXFT_BIGTABLE_CHEM_NAME_SPL"
    storage         "IDXFT_BIGTABLE_CHEM_NAME_STO"
    index set       "IDXFT_BIGTABLE_CHEM_NAME_IXS"
  ')
/

begin
  ctx_output.end_log;
end;
/



