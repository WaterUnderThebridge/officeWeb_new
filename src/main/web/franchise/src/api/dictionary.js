import request from '@/utils/request'


export function dicList(query) {
  return request({
    url: `/dic/${query.table}/list`,
    method: 'get',
    params: {content:query.content}
  })
}
export function dicAdd(query) {
  return request({
    url: `/dic/${query.table}/add`,
    method: 'get',
    params: query
  })
}
export function dicUpdate(query) {
  return request({
    url: `/dic/${query.table}/update`,
    method: 'get',
    params: query
  })
}
export function dicDel(query) {
  return request({
    url: `/dic/${query.table}/del`,
    method: 'get',
    params: query
  })
}
 
