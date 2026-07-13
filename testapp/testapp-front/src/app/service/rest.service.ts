import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {Observable, Subject} from 'rxjs';
import { Message } from '../model/dtos';

export class RestOptions {
  constructor(headers: HttpHeaders, withCredentials = true) {
    this.headers = headers;
    this.withCredentials = withCredentials;
  }

  headers?: HttpHeaders |
    {
      [header: string]: string | string[];
    };
  observe?: 'body';
  params?: HttpParams
    | {
    [param: string]: string | string[];
  };
  reportProgress?: boolean;
  responseType?: 'json';
  withCredentials?: boolean;
}

@Injectable({
  providedIn: 'root',
})
export class RestService {
  jsRestOptions = new RestOptions(new HttpHeaders()
    .set('Content-Type', 'application/json')
    .set('Access-Control-Allow-Origin', '*')
    .set('Timeout', '360000')
    .set('Cache-control', 'no-cache')
    .set('Pragma', 'no-cache')
    .set('Expires', '-1')
    .set('If-Modified-Since', '0'));
  constructor(private readonly httpClient: HttpClient) {
  }

    getMqMessage(nb: number): Observable<Message[]> {
        const params = this.createParams({ nb });
        return this.httpClient.get<Message[]>(`/rest/mq/last`, params);
    }

    private createParams(params: any, restOptions: RestOptions = this.jsRestOptions): RestOptions {
        let copy: RestOptions = { ...restOptions };
        if (params) {
            copy.params = new HttpParams({
                fromObject: params
            });
        }
        return copy;
    }
}
