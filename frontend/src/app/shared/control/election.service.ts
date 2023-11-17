import { Injectable } from '@angular/core';
import {Election} from "../entity/election-model";

@Injectable({
  providedIn: 'root'
})
export class ElectionService {
  elections:Election[]=
    [
      new Election("1",new Date("01/02/2005"),new Date("01/02/2006"),"points"),
      new Election("2",new Date("02/02/2005"),new Date("02/02/2005"),"cross")
  ];
}
