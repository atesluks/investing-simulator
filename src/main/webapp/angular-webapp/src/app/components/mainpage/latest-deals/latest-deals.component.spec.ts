import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LatestDealsComponent } from './latest-deals.component';

describe('LatestDealsComponent', () => {
  let component: LatestDealsComponent;
  let fixture: ComponentFixture<LatestDealsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ LatestDealsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LatestDealsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
