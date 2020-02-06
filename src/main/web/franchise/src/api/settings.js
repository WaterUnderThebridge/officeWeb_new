import request from '@/utils/request'


const requestUrl="https://bbk.800app.com/uploadfile/staticresource/238592/279833/api_auto_json.aspx";
const ecUrl="https://bbk.800app.com/uploadfile/staticresource/238592/279833/dataInterface_sync_ec.aspx";
const sql_sync="update yzx set crmzdy_82069676=quot;@resquot; from crm_zdytable_238592_26580_238592_view yzx where crmzdy_82004682=@gymcode;select 0 errcode for json path;" 

export function fetchList(cxt) {
  let condition=[];
  if(cxt.account.acl!="系统管理员"&&charindex('中心运营总监',cxt.account.acl)==0){
    condition.push("crm_syrID="+cxt.account.id)
  }
  if(cxt.account.acl!="系统管理员"){
    condition.push("crm_syrID='"+JSON.stringify(cxt.account.gyms[0])+"");
  }
  if(cxt.listQuery.dtzx){
    condition.push("create_time between '@dtstart' and '@dtend'".replace("@dtstart",cxt.listQuery.dtzx[0]).replace("@dtend",cxt.listQuery.dtzx[1]));
  }
  condition=condition.concat(cxt.advSearchWhere);
  cxt.advSearchWhere=[];
  let sql=sql_list.replace("@condition",condition.length==0?'1=1':condition.join(" and "));
  
  let option={pageSize:cxt.listQuery.limit,pageNow:cxt.listQuery.page,condition:cxt.searchWhere,order:cxt.listQuery.sort};
  sql = cxt.fn_pager(sql,option);
  return request({
    baseURL: requestUrl,
    method: 'get',
    params: {sql1:sql}
  })
}
export function fetchChannel(cxt){
  let sql = sql_channel.replace("@idgym",cxt.temp.gym_selected&&cxt.temp.gym_selected.id||0)
  return request({
    baseURL: requestUrl,
    method: 'get',
    params: {sql1:sql}
  })
}

export function createClient(cxt){
  console.log(cxt.account)
  var sql = sql_client_crt;
  sql=sql.replace("@form",JSON.stringify(cxt.temp))
  sql=sql.replace("@iduser",cxt.account&&cxt.account.id||'')

  return request({
    baseURL: requestUrl,
    method: 'get',
    params: {sql1:sql}
  })
}


export function syncOASIS(sql){
  return request({
    baseURL: ecUrl,
    method: 'get',
    params: {sql1:sql}
  })
}

export function sync(cxt){
  console.log(cxt.account)
    var gymcode=cxt.account&&cxt.account.gyms[0]&&cxt.account.gyms[0].gymcode;
    var res = JSON.stringify(cxt.labelGrps);
    var sql = sql_sync;
    sql = sql.replace("@res",res).replace("@gymcode",gymcode)
    sql = sql&&this.convertor.toUni(sql);
    return request({
        baseURL: requestUrl,
        method: 'get',
        params: {sql1:sql}
    })
}

