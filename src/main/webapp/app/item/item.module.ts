import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TbsSharedModule } from 'app/shared/shared.module';
import { ItemComponent } from './item.component';
import { ItemDetailComponent } from './item-detail.component';
import { ItemUpdateComponent } from './item-update.component';
import { ItemDeletePopupComponent, ItemDeleteDialogComponent } from './item-delete-dialog.component';
import { itemRoute, itemPopupRoute } from './item.route';
import {MaterialModule} from 'app/app.module';
import {NgMultiSelectDropDownModule} from 'ng-multiselect-dropdown';

const ENTITY_STATES = [...itemRoute, ...itemPopupRoute];

@NgModule({
  imports: [TbsSharedModule, RouterModule.forChild(ENTITY_STATES), MaterialModule, NgMultiSelectDropDownModule],
  declarations: [ItemComponent, ItemDetailComponent, ItemUpdateComponent, ItemDeleteDialogComponent, ItemDeletePopupComponent],
  entryComponents: [ItemComponent, ItemUpdateComponent, ItemDeleteDialogComponent, ItemDeletePopupComponent]
})
export class TbsposItemModule {}
