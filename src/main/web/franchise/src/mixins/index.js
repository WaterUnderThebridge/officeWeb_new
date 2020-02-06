import { mapGetters } from 'vuex';
import request from '@/utils/request';
const requestUrl="https://bbk.800app.com/uploadfile/staticresource/238592/279833/api_auto_json.aspx";
import { userList } from '@/api/user' 
export default{
    data(){
       return {
           gym:undefined,
           gyms:undefined,
       }
    },
    computed: {
        ...mapGetters([
          'userid'
        ])
    },
    methods:{
        getLs(gym){
            let sql = "select ls.crm_qm username,ls.id from crm_yh_238592_view ls where crm_yhname not like '50%' and crm_yiqiyong=1 and charindex('@tbl_gym',crmzdy_81611236)>0 order by username for json auto";
            sql = sql.replace("@tbl_gym",gym&&gym.name);

            let params={sql1:sql},self=this;
            request({
                baseURL: requestUrl,
                method: 'post',
                params
              }).then(response => {
                self.tutors = response.data
              })
        },
        getGym(){
            if(this.account.acl!=='系统管理员'){
               this.gyms = response.data;
               this.gym  = this.gyms&&this.gyms[0];
               return;
            }
            let sql="select (select crm_name name,crmzdy_80620116 code,id from crm_zdytable_238592_23594_238592_view  order by name for json auto);";
            let params={sql1:sql},self=this;
            request({
                baseURL: requestUrl,
                method: 'post',
                params
              }).then(response => { 
                self.gyms = response.data;
                self.gym  = self.gyms&&self.gyms[0];
                self.gym&&self.getLs(self.gym);
              })
        },
        get_tag(gymcode){
            gymcode=gymcode||(this.account&&this.account.gyms&&this.account.gyms[0]&&this.account.gyms[0].code);
            let sql="select 0 errcode,'ok'errmsg,isnull((select JSON_query(isnull(nullif(crmzdy_82069676,''),quot;[]quot;)) tag from crm_zdytable_238592_26580_238592_view yzx where crmzdy_82004682='@gymcode'),'[]')arr,'@sql'sql for json path,without_array_wrapper";
            sql = sql.replace("@gymcode",gymcode);
            let params={sql1:sql},self=this;
            request({
                baseURL: requestUrl,
                method: 'post',
                params
            }).then(response => { 
                if(response.data&&response.data.errcode==0){
                    self.labelGrps = response.data.arr;
                    console.log(self.labelGrps)
                    if(self.tag_select){
                        self.tag_select();
                    }
                }
            })
        },
        obj2Arr(obj){
            var arr = []
            for (let i in obj) {
                arr.push("'"+obj[i]+"' "+i)
            }
            return arr;
        },
        json2Sql(data,fn){
             fn=fn||self.obj2Arr;
             var self=this;
             var sql="";
             data.map(function(d){
                sql += "select "+(typeof d =='object'?fn(d).join(","):"'"+d+"' name")+" union all ";
             })
             return  sql.slice(0,-10);
        },
        json(obj_str,key){
            console.error(obj_str)
          if(!key) return obj_str&&JSON.stringify(this.obj2Arr);
          if (typeof obj_str=="object") return obj_str[key];
          if (typeof obj_str!="string") { 
             if(obj_str!="") console.error(obj_str);;
             return "";
          }
          if(obj_str.indexOf("}")!=-1){
            var obj=JSON.parse(obj_str);
            if(obj){
                return obj[key];
            }
          }
          return '';
        },
        urlView:function(row){
            if(row.idjt&&row.idjt!="undefined"&&row.idjt!=""&&row.idjt!=0){
                var u= "<a title='点击进入家庭' style='text-decoration:underline' href='https://bbk.800app.com/index.jsp?mlist=1&mfs1=crm_sj&mid=@idjt&menu=3' target='_blank'>@phone</a>";
                return u.replace('@idjt',row.idjt).replace('@phone',row.phone);
            }
            return row.phone;
        },
        urlView_zx:function(label){
            let tmp=function (row,label){
                if(row.idzx){
                    var u ='<a title="点击进入咨询中心" href="https://bbk.800app.com/index.jsp?mlist=1&amp;mfs1=crm_zdytable_238592_25111&amp;mid=@idzx&amp;menu=3" target="_blank">@label</a>';
                    return u.replace('@idzx',row.idzx).replace('@label',row[label]||"查看");
                }
                return '查看';
            }
            return function(row){
                return tmp(row,label);
            };
        },
        getUsers(){
          let self=this;
          userList().then((response)=>{
                if(response.code==0){
                  self.users=response.data;
                }
          });
        },
        download(url){
            var x=new XMLHttpRequest();
                x.open("GET", url, true);
                x.responseType = 'blob';
                x.onload=function(e){
                    var url = window.URL.createObjectURL(x.response)
                    var a = document.createElement('a');
                    a.href = url
                    a.download = ''
                    a.click()
                }
                x.send();
        },
        join(arr){
            if(arr&&arr[0]){
               if(typeof arr[0]=='string') return "'"+arr.join("','")+"'";
               return arr.join(",");
            }
            return '';
        },
        formatJson(filterVal, jsonData) {
          return jsonData.map(v => filterVal.map(j => {
            if (j === 'timestamp') {
              return parseTime(v[j])
            } else {
              return v[j]
            }
          }))
        }
    }
    
}