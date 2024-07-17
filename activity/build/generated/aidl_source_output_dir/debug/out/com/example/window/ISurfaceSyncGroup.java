/*
 * This file is auto-generated.  DO NOT MODIFY.
 */
package com.example.window;
public interface ISurfaceSyncGroup extends android.os.IInterface
{
  /** Default implementation for ISurfaceSyncGroup. */
  public static class Default implements com.example.window.ISurfaceSyncGroup
  {
    @Override public boolean onAddedToSyncGroup(android.os.IBinder parentSyncGroupToken, boolean parentSyncGroupMerge) throws android.os.RemoteException
    {
      return false;
    }
    @Override public boolean addToSync(com.example.window.ISurfaceSyncGroup surfaceSyncGroup, boolean parentSyncGroupMerge) throws android.os.RemoteException
    {
      return false;
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements com.example.window.ISurfaceSyncGroup
  {
    /** Construct the stub at attach it to the interface. */
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an com.example.window.ISurfaceSyncGroup interface,
     * generating a proxy if needed.
     */
    public static com.example.window.ISurfaceSyncGroup asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof com.example.window.ISurfaceSyncGroup))) {
        return ((com.example.window.ISurfaceSyncGroup)iin);
      }
      return new com.example.window.ISurfaceSyncGroup.Stub.Proxy(obj);
    }
    @Override public android.os.IBinder asBinder()
    {
      return this;
    }
    @Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
    {
      java.lang.String descriptor = DESCRIPTOR;
      if (code >= android.os.IBinder.FIRST_CALL_TRANSACTION && code <= android.os.IBinder.LAST_CALL_TRANSACTION) {
        data.enforceInterface(descriptor);
      }
      switch (code)
      {
        case INTERFACE_TRANSACTION:
        {
          reply.writeString(descriptor);
          return true;
        }
      }
      switch (code)
      {
        case TRANSACTION_onAddedToSyncGroup:
        {
          android.os.IBinder _arg0;
          _arg0 = data.readStrongBinder();
          boolean _arg1;
          _arg1 = (0!=data.readInt());
          boolean _result = this.onAddedToSyncGroup(_arg0, _arg1);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_addToSync:
        {
          com.example.window.ISurfaceSyncGroup _arg0;
          _arg0 = com.example.window.ISurfaceSyncGroup.Stub.asInterface(data.readStrongBinder());
          boolean _arg1;
          _arg1 = (0!=data.readInt());
          boolean _result = this.addToSync(_arg0, _arg1);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        default:
        {
          return super.onTransact(code, data, reply, flags);
        }
      }
      return true;
    }
    private static class Proxy implements com.example.window.ISurfaceSyncGroup
    {
      private android.os.IBinder mRemote;
      Proxy(android.os.IBinder remote)
      {
        mRemote = remote;
      }
      @Override public android.os.IBinder asBinder()
      {
        return mRemote;
      }
      public java.lang.String getInterfaceDescriptor()
      {
        return DESCRIPTOR;
      }
      @Override public boolean onAddedToSyncGroup(android.os.IBinder parentSyncGroupToken, boolean parentSyncGroupMerge) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeStrongBinder(parentSyncGroupToken);
          _data.writeInt(((parentSyncGroupMerge)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_onAddedToSyncGroup, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public boolean addToSync(com.example.window.ISurfaceSyncGroup surfaceSyncGroup, boolean parentSyncGroupMerge) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeStrongInterface(surfaceSyncGroup);
          _data.writeInt(((parentSyncGroupMerge)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_addToSync, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
    }
    static final int TRANSACTION_onAddedToSyncGroup = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_addToSync = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
  }
  public static final java.lang.String DESCRIPTOR = "com.example.window.ISurfaceSyncGroup";
  public boolean onAddedToSyncGroup(android.os.IBinder parentSyncGroupToken, boolean parentSyncGroupMerge) throws android.os.RemoteException;
  public boolean addToSync(com.example.window.ISurfaceSyncGroup surfaceSyncGroup, boolean parentSyncGroupMerge) throws android.os.RemoteException;
}
