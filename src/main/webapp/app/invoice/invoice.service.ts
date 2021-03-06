import {Injectable} from '@angular/core';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {Observable} from 'rxjs';
import * as moment from 'moment';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import {map} from 'rxjs/operators';

import {SERVER_API_URL} from 'app/app.constants';
import {createRequestOption} from 'app/shared/util/request-util';
import {IInvoice} from 'app/shared/model/invoice.model';
import {DataTableInput} from 'app/shared/model/datatable/datatable-input';
import {_tbs} from 'app/shared/util/tbs-utility';
import {Pageable} from 'app/shared/model/pageable';
import {IInvoiceSearchRequest} from "app/shared/model/invoice-serach-request";

type EntityResponseType = HttpResponse<IInvoice>;
type EntityArrayResponseType = HttpResponse<IInvoice[]>;

@Injectable({ providedIn: 'root' })
export class InvoiceService {
  public resourceUrl = SERVER_API_URL + 'api/invoices';

  constructor(protected http: HttpClient) {}

  getList(datatableInput: DataTableInput): Observable<Pageable<IInvoice>> {
    return this.http.get<Pageable<IInvoice>>(`${this.resourceUrl}/datatable?${_tbs.serializeDataTableRequest(datatableInput)}`);
  }
//datatable?${_tbs.serializeDataTableRequest(datatableInput)} || datatableInput: DataTableInput,
  getInvoiceBySearch(invoiceSearchRequest : IInvoiceSearchRequest): Observable<Pageable<IInvoice>> {
    return this.http.post<Pageable<IInvoice>>(`${this.resourceUrl}/search`,invoiceSearchRequest);
  }

  getTripAudit(id: number) {
    return this.http.get<any>(`${this.resourceUrl}/audit/${id}`);
  }

  create(invoice: IInvoice): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(invoice);
    return this.http
      .post<IInvoice>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(invoice: IInvoice): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(invoice);
    return this.http
      .put<IInvoice>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IInvoice>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IInvoice[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  queryByPaymentStatus(status?: any, req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IInvoice[]>(`${this.resourceUrl}/paymentStatus/${status}`, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(invoice: IInvoice): IInvoice {
    const copy: IInvoice = Object.assign({}, invoice, {
      dueDate: invoice.dueDate != null && invoice.dueDate.isValid() ? invoice.dueDate.toJSON() : null
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dueDate = res.body.dueDate != null ? moment(res.body.dueDate) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((invoice: IInvoice) => {
        invoice.dueDate = invoice.dueDate != null ? moment(invoice.dueDate) : null;
      });
    }
    return res;
  }
}
