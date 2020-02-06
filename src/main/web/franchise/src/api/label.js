import request from '@/utils/request'


export function labelList() {
  return request({
    url: '/label/list',
    method: 'get'
  })
}
export function labelAdd(query) {
  return request({
    url: '/label/add',
    method: 'get',
    params: query
  })
}
export function labelDel(query) {
  return request({
    url: '/label/del',
    method: 'get',
    params: query
  })
}

export function labeForFranApp(FranAppId) {
  return request({
    url: '/label/franApp',
    method: 'get',
    params: {FranAppId}
  })
}

 
