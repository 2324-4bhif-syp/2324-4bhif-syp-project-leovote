import {retry} from "rxjs";

export class TablePagination {
  private data: any[];
  private _pageSize: number;
  private _currentPage: number;

  constructor(data: any[], pageSize: number) {
    this.data = data;
    this._pageSize = pageSize;
    this._currentPage = 1;
  }

  get maxPage() {
    return Math.ceil(this.data.length / this._pageSize);
  }

  goToFirstPage() {
    this._currentPage = 1;
  }

  goToPreviousPage() {
    if (this._currentPage > 1) {
      this._currentPage--;
    }
  }

  goToNextPage() {
    if (this._currentPage < this.maxPage) {
      this._currentPage++;
    }
  }

  goToLastPage() {
    this._currentPage = this.maxPage;
  }

  setPageSize(newSize: number) {
    this._pageSize = newSize;
    this._currentPage = 1; // Reset to first page when page size changes
  }

  getCurrentPageData() {
    const start = (this._currentPage - 1) * this._pageSize;
    return this.data.slice(start, start + this._pageSize);
  }

  get currentPage(): number {
    return this._currentPage;
  }

  getPageCount(): number {
    const pageCount = Math.ceil(this.data.length / this._pageSize);

    if (pageCount < this._currentPage) {
      return this._currentPage;
    }

    return pageCount;
  }

  get pageSize(): number {
    return this._pageSize;
  }

  getIndexOffset(): number {
    return (this.currentPage - 1) * this.pageSize;
  }

  deleteRow(index: number) {
    this.data.splice(index, 1);
  }
}
