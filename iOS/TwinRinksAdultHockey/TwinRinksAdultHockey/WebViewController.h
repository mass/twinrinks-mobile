//
//  WebViewController.h
//  TwinRinksAdultHockey
//
//  Created by Andrew on 1/5/13.
//  Copyright (c) 2013 GigaStorm. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface WebViewController : UIViewController {
    //IBOutlet UIWebView *myWebView;
}
@property (strong,nonatomic) UIWebView *myWebView;

-(IBAction)refreshData:id;
-(IBAction)settingsButtonPressed:id;
-(IBAction)helpButtonPressed:id;
-(void)initButtons;

@end
