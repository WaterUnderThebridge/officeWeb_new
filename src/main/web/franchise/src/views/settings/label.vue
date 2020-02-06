<template>
  <div>
    <el-menu :default-active="activeIndex" class="el-menu-demo" mode="horizontal" @select="handleSelect">
        <el-menu-item index="0">标签分组</el-menu-item>
        <el-menu-item index="1">客户分组</el-menu-item>
    </el-menu>

    <el-card class="box-card">
        <component :is="item[activeIndex]"></component>
    </el-card> 
</div>

 
</template>

<script>
  import TagMgr from "./TagMgr";
  import clientGrp from "./clientGrp";
  export default {
    data() {
      return {
        dialogFormVisible:false,
        activeIndex: '0',
        item:['TagMgr','clientGrp']
      };
    },
    components: {
       TagMgr,
       clientGrp
    },
    methods: {
      handleSelect(key, keyPath) {
        //console.log(key, keyPath);
        this.activeIndex=key;
      }
    },
    mounted(){
 
    }
  }
</script>


<style lang="scss" scoped>
  .color{
    color:#409eff;
    background-color: #ecf5ff;
    border-color: #c6e2ff;
  }
  .box-card{
    .w-50{
      width: 48%;
      display: inline-block;
    }
    // width: 70%;
  }
  .card-right{
    height:100%;
    box-shadow:none;
    border-bottom: none;
    border-right: none;
    position: relative;
   
  }
  .btnn{
      margin: 10px 5px 5px 10px !important;
    }
  
  .clearfix:before,
  .clearfix:after {
    display: table;
    content: "";
  }
  .clearfix:after {
    clear: both
  }

</style>

