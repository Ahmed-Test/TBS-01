import {ITax} from 'app/shared/model/tax.model';
import {ICategory} from 'app/shared/model/category.model';
import {IClient} from 'app/shared/model/client.model';

export interface IItem {
  id?: number;
  code?: number;
  name?: string;
  description?: string;
  price?: number;
  defaultQuantity?: number;
  taxes?: ITax[];
  categoryId?: number;
  clientId?: number;
  category?: ICategory;
  client?: IClient;
  flexiblePrice?: boolean;


}

export class Item implements IItem {
  constructor(
    public id?: number,
    public name?: string,
    public description?: string,
    public price?: number,
    public defaultQuantity?: number,
    public taxes?: ITax[],
    public categoryId?: number,
    public clientId?: number,
    category?: ICategory,
    client?: IClient,
    code?: number,
    flexiblePrice?: boolean


) {}
}
