export default{
    methods:{
        obj2Arr(obj){
            var arr = []
            for (let i in obj) {
                arr.push("'"+obj[i]+"' "+i)
            }
            //console.log(arr.length)
            //if(arr.length==15) console.log(JSON.stringify(obj))
            return arr;
        },
        json2Sql(data){
             var self=this;
             var sql="";
             data.map(function(d){
                 sql += "select "+self.obj2Arr(d).join(",")+" union all ";
             })
             return  sql.slice(0,-10);
        },
        json(obj_str,key){
          if (typeof obj_str=="object") return obj_str[key];
          if (typeof obj_str!="string") return "invalid string"
          if(this.trim(obj_str)!=""){
            var obj=JSON.parse(obj_str);
            if(obj){
                return obj[key];
            }
          }
          return '';
        },
        tag_find(key,arr){
            if(typeof arr != "array") return -1
            return arr.findIndex(function(t){
              return t.name==key;
            })
        },
        get_tag(){
            var self=this;
            self.$store.commit({
                type:"get_data",
                sql:"select isnull(crmzdy_82069676,quot;[]quot;) tag from crm_zdytable_238592_26580_238592_view yzx where crmzdy_82004682=@gymcode",
                func:function(data){
                //console.log(JSON.stringify(data)
                    if(data){
                        self.labelGrps=data;
                        if(self.tag_select){
                           self.tag_select();
                        }
                    }else{
                        self.labelGrps=[]; 
                    }
                }
            })
        },
        get_clie_group(){
            var self=this;
            self.$store.commit({
                type:"get_data",
                sql:"select isnull(crmzdy_82069677,quot;[]quot;) groups from crm_zdytable_238592_26580_238592_view yzx where crmzdy_82004682=@gymcode",
                func:function(data){
                    if(data){
                        self.clientlGrps=data;
                    }else{
                        self.clientlGrps=[]; 
                    }
                    //console.log("down",JSON.stringify(self.clientlGrps))
                }
            })
        },
        urlView:function(row){
            if(row.idjt&&row.idjt!="undefined"&&row.idjt.trim()!=""){
		    var u= "<a title='点击进入家庭' href='https://bbk.800app.com/index.jsp?mlist=1&mfs1=crm_sj&mid=@idjt&menu=3' target='_blank'>@phone</a>";
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
        }

    }
    
}