var myFun = {
    install: function (Vue, options) {
        Vue.prototype.convertor={
            toUni:function (str) {
              str=str.replace(/'/ig,"quot;").replace(/\+/ig,"add;");
              return escape(str).toLocaleLowerCase().replace(/%u/gi, '\\u');
            },
            toGBK:function (str) {
              return unescape(str.replace(/\\u/gi, '%u'));
            }
        }
        Vue.prototype.clearNullAr=function(arr){
            for(var i=0,len=arr.length;i<len;i++){
                if(!arr[i]||arr[i]==''||arr[i] === undefined){
                    arr.splice(i,1);
                    len--;
                    i--;
                }
            }
            return arr;
        }
        Vue.prototype.extend=function(a,b,force=false){
            console.log(JSON.stringify(a))
            if(!(typeof a ==="object" && typeof b ==="object")){
                console.error("first two parameters must be object!");
                return {};
            }
            for(var prop in b){
                //console.log(prop)
                if(force || !a.hasOwnProperty(prop)) {
                    //console.log("update "+prop)
                    a[prop] = b[prop];
                };
            }
            return a;
        }
        Vue.prototype.fmtDt=fmtDt;
        Vue.prototype.isEmpty=isEmpty;
        Vue.prototype.fn_pager=fn_pager;
        Vue.filter('t', trim);
        
        function fn_pager(str,option){
            if(!(typeof option==="object"&&option.pageNow&&option.pageSize)){throw new Error("函数fn_pager的option参数不正确"); }
            var header="with ";
            if(/with.+as/.test(str)){
              header=","
            } 
            if(/^(.*)(select\s*?)\/\*main\*\/(.*?)[;]?$/.test(str)){
                var pager = "select * from p order by @order id";
                pager += " offset "+(option.pageNow-1)*option.pageSize+" rows fetch next "+option.pageSize+" rows only"
                str = RegExp.$1+header+"p1 as("+RegExp.$2+RegExp.$3+"),p as(select * from p1 @c) select (select 0 errcode,'ok'errmsg,(select count(1) from p)total,isnull((@pager for json path),'[]')arr,'@sql'sql from (select 1 x)x for json path,without_array_wrapper)";
                str = str.replace("@c",!option.condition?"":"where "+option.condition).replace("@pager",pager).replace("@order",option.order||'');
            }else{
                throw new Error("sql没有main标识！")
            }
            return str;
        }
        function trim(obj) {
            if(obj){
              return obj.replace(/^[\s\uFEFF\xA0]+|[\s\uFEFF\xA0]+$/g, '');
            }
            return '';
        };
        function fmtDt(fmt){ 
            let tmp =function () {
                //取代普通域变量,使用对象arguments+bind闭函数，相当于php function use();
                let dt=arguments[1];
                if(!dt) return '';
                let fmt=arguments[0]|| "yyyy-MM-dd hh:mm:ss";
                if(typeof dt!=="object"){
                    //ie不兼容问题
                    if(dt.indexOf("T")==-1) dt = dt.replace(/-/g,"/")
                    dt = new Date(dt||'');
                }
                if (/(y+)/.test(fmt)) {
                    fmt = fmt.replace(RegExp.$1, (dt.getFullYear() + '').substr(4 - RegExp.$1.length));
                }
                let o = {
                    'M+': dt.getMonth() + 1,
                    'd+': dt.getDate(),
                    'h+': dt.getHours(),
                    'm+': dt.getMinutes(),
                    's+': dt.getSeconds()
                };
                for (let k in o) {
                    if (new RegExp(`(${k})`).test(fmt)) {
                        let str = o[k] + '';
                        fmt = fmt.replace(RegExp.$1, (RegExp.$1.length === 1) ? str : padLeftZero(str));
                    }
                }
                function padLeftZero(str) {
                    return ('00' + str).substr(str.length);
                }
                return fmt;
            } 
            return tmp.bind(undefined,fmt);
        }
        function tags_format(tags){
          return tags.reduce(function(prev, cur, index, array){
                    if(typeof prev==="object"){
                       return prev.key+":"+prev.label+";"+cur.key+":"+cur.label;
                    }else{
                      return prev+";"+cur.key+":"+cur.label;
                    }
                },"")
        }
        function padLeftZero(str) {
            return ('00' + str).substr(str.length);
        }
        function isEmpty(obj)
        {
            for (var name in obj) 
            {
                return false;
            }
            return true;
        }
    }
  }

export {
    myFun
}